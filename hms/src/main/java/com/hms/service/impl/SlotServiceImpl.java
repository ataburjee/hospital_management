package com.hms.service.impl;

import com.hms.dto.RecurringSlotRequest;
import com.hms.dto.SlotBookingRequest;
import com.hms.dto.SlotCreateRequest;
import com.hms.model.*;
import com.hms.repository.AppointmentSlotRepository;
import com.hms.repository.DoctorRepository;
import com.hms.repository.PatientRepository;
import com.hms.repository.TimeSlotRepository;
import com.hms.service.SlotService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

    private final DoctorRepository doctorRepository;
    private final AppointmentSlotRepository appointmentSlotRepository;
    private final PatientRepository patientRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Override
    @Transactional
    public String createSlot(SlotCreateRequest request) {
        // Step 1: Check doctor exists
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Step 2: Check conflict
        boolean exists = appointmentSlotRepository.existsByDoctorIdAndDateAndTimeRange(
                doctor.getId(),
                request.getDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (exists) {
            throw new RuntimeException("Slot conflict: overlapping with existing slot");
        }

        // Step 3: Create Slot
        AppointmentSlot slot = AppointmentSlot.builder()
                .doctor(doctor)
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .slotType(request.getSlotType())
                .slotStatus(SlotStatus.AVAILABLE)
                .tag(request.getTag())
                .note(request.getNote())
                .colorCode(request.getColorCode())
                .recurrencePattern(request.getRecurrencePattern())
                .build();

        AppointmentSlot savedSlot = appointmentSlotRepository.save(slot);
        if (slot.getSlotType() == SlotType.CONSULTATION) {
            generateTimeSlots(savedSlot, 15);
        }
        return "Slot created successfully";
    }

    private void generateTimeSlots(AppointmentSlot slot, int durationInMinutes) {
        LocalTime pointer = slot.getStartTime();
        int queue = 1;

        while (pointer.plusMinutes(durationInMinutes).isBefore(slot.getEndTime().plusSeconds(1))) {
            LocalTime next = pointer.plusMinutes(durationInMinutes);

            TimeSlot timeSlot = TimeSlot.builder()
                    .startTime(pointer)
                    .endTime(next)
                    .appointmentSlot(slot)
                    .queueNumber(queue++)
                    .slotStatus(SlotStatus.AVAILABLE)
                    .build();

            timeSlotRepository.save(timeSlot);
            pointer = next;
        }
    }


    @Override
    @Transactional
    public String createRecurringSlots(RecurringSlotRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        int createdCount = 0;
        int skippedCount = 0;

        // Iterate over all dates between start and end
        for (LocalDate date = request.getStartDate(); !date.isAfter(request.getEndDate()); date = date.plusDays(1)) {
            if (!request.getRepeatOn().contains(date.getDayOfWeek())) continue;

            // Generate time chunks
            LocalTime slotStart = request.getStartTime();
            LocalTime slotEnd = request.getEndTime();

            while (slotStart.plusMinutes(request.getSlotDurationInMinutes()).isBefore(slotEnd.plusSeconds(1))) {
                LocalTime nextSlotEnd = slotStart.plusMinutes(request.getSlotDurationInMinutes());

                boolean exists = appointmentSlotRepository.existsByDoctorIdAndDateAndTimeRange(
                        doctor.getId(), date, slotStart, nextSlotEnd
                );

                if (!exists) {
                    AppointmentSlot slot = AppointmentSlot.builder()
                            .doctor(doctor)
                            .date(date)
                            .startTime(slotStart)
                            .endTime(nextSlotEnd)
                            .slotType(request.getSlotType())
                            .slotStatus(SlotStatus.AVAILABLE)
                            .tag(request.getTag())
                            .note(request.getNote())
                            .colorCode(request.getColorCode())
                            .recurrencePattern("RECURRING")
                            .build();
                    appointmentSlotRepository.save(slot);
                    createdCount++;
                } else {
                    skippedCount++;
                }

                slotStart = nextSlotEnd;
            }
        }

        return "Recurring slots created: " + createdCount + ", skipped due to conflict: " + skippedCount;
    }

    @Override
    @Transactional
    public String bookSlot(SlotBookingRequest request) {
        AppointmentSlot slot = appointmentSlotRepository.findById(request.getAppointmentSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        // Get next available 15-min sub-slot
        List<TimeSlot> timeSlots = timeSlotRepository.findByAppointmentSlotAndSlotStatusOrderByQueueNumber(
                slot, SlotStatus.AVAILABLE
        );

        if (timeSlots.isEmpty()) {
            throw new RuntimeException("All time slots are booked");
        }

        TimeSlot nextSlot = timeSlots.get(0);

        Patient patient = patientRepository.findById(request.getPatientId()).orElse(
                Patient.builder().name(request.getPatientName()).build()
        );
        patientRepository.save(patient);

        // Assign patient to slot
        nextSlot.setPatient(patient);
        nextSlot.setSlotStatus(SlotStatus.BOOKED);
        patient.setTimeSlot(nextSlot);
        patient.setAppointmentNumber(nextSlot.getQueueNumber());

        patientRepository.save(patient);
        timeSlotRepository.save(nextSlot);

        return "Patient " + patient.getName() + " booked at " + nextSlot.getStartTime() +
                " (Queue #" + nextSlot.getQueueNumber() + ")";
    }

}
