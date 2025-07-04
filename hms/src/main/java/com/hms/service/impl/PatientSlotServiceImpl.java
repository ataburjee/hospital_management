package com.hms.service.impl;

import com.hms.dto.*;
import com.hms.enums.SlotStatus;
import com.hms.enums.SlotType;
import com.hms.model.*;
import com.hms.repository.AppointmentSlotRepository;
import com.hms.repository.DoctorRepository;
import com.hms.repository.PatientRepository;
import com.hms.repository.TimeSlotRepository;
import com.hms.service.PatientSlotService;
import com.hms.util.Utility;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientSlotServiceImpl implements PatientSlotService {

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
                .id(Utility.generateId())
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

            PatientTimeSlot patientTimeSlot = PatientTimeSlot.builder()
                    .id(Utility.generateId())
                    .startTime(pointer)
                    .endTime(next)
                    .appointmentSlot(slot)
                    .queueNumber(queue++)
                    .slotStatus(SlotStatus.AVAILABLE)
                    .build();

            timeSlotRepository.save(patientTimeSlot);
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
                            .id(Utility.generateId())
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
        List<PatientTimeSlot> patientTimeSlots = timeSlotRepository.findByAppointmentSlotAndSlotStatusOrderByQueueNumber(
                slot, SlotStatus.AVAILABLE
        );

        if (patientTimeSlots.isEmpty()) {
            throw new RuntimeException("Oops! all slots are booked");
        }

        PatientTimeSlot nextSlot = patientTimeSlots.getFirst();

        Patient patient = patientRepository.findById(request.getPatientId()).orElse(
                Patient.builder().id(Utility.generateId(Utility.PATIENT)).name(request.getPatientName()).build()
        );
        patientRepository.save(patient);

        // Assign patient to slot
        nextSlot.setPatient(patient);
        nextSlot.setSlotStatus(SlotStatus.BOOKED);
        patient.setPatientTimeSlot(nextSlot);
        patient.setAppointmentNumber(nextSlot.getQueueNumber());

        patientRepository.save(patient);
        timeSlotRepository.save(nextSlot);

        return "Patient " + patient.getName() + " booked at " + nextSlot.getStartTime() +
                " (Queue #" + nextSlot.getQueueNumber() + ")";
    }

    @Override
    public List<AvailableTimeSlotResponse> getAvailableTimeSlots(String doctorId, LocalDate date) {
        List<AppointmentSlot> slots = appointmentSlotRepository.findByDoctorIdAndDate(doctorId, date);
        if (slots.isEmpty()) {
            return Collections.emptyList();
        }

        return slots.stream()
                .flatMap(slot -> {
                    List<PatientTimeSlot> patientTimeSlots = timeSlotRepository.findByAppointmentSlot(slot).orElseThrow(
                            () -> new RuntimeException("No slots found")
                    );
                    System.out.println("AppointmentSlot ID: " + slot.getId() + " -> TimeSlots: " + patientTimeSlots.size());
                    return patientTimeSlots.stream();
                })
                .map(ts -> new AvailableTimeSlotResponse(
                        ts.getId(),
                        ts.getStartTime().toString(),
                        ts.getEndTime().toString(),
                        ts.getSlotStatus()
                ))
                .toList();
    }

    @Override
    public void updateAppointmentSlot(String id, SlotUpdateRequest request) {
        AppointmentSlot slot = appointmentSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        boolean conflict = appointmentSlotRepository.existsByDoctorIdAndDateAndTimeRange(
                slot.getDoctor().getId(),
                request.getDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (conflict) {
            throw new RuntimeException("Slot conflict with existing schedule");
        }

        slot.setDate(request.getDate());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        appointmentSlotRepository.save(slot);
    }

    @Override
    public void deleteSlotWithTimeSlots(String appointmentSlotId) {
        AppointmentSlot slot = appointmentSlotRepository.findById(appointmentSlotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        // Delete associated time slots first (if cascade not working)
        List<PatientTimeSlot> patientTimeSlots = timeSlotRepository.findByAppointmentSlotOrderByQueueNumberAsc(slot);
        timeSlotRepository.deleteAll(patientTimeSlots);

        appointmentSlotRepository.delete(slot);
    }

    @Override
    public void bulkUpdateSlotDurations(BulkUpdateSlotDurationRequest request) {
        List<AppointmentSlot> slots = appointmentSlotRepository
                .findAllByDoctorIdAndDateBetween(request.getDoctorId(), request.getStartDate(), request.getEndDate());

        for (AppointmentSlot slot : slots) {
            List<PatientTimeSlot> patientTimeSlots = timeSlotRepository.findByAppointmentSlotOrderByQueueNumberAsc(slot);
            LocalTime start = slot.getStartTime();

            for (int i = 0; i < patientTimeSlots.size(); i++) {
                PatientTimeSlot ts = patientTimeSlots.get(i);
                ts.setStartTime(start);
                ts.setEndTime(start.plusMinutes(request.getNewDurationInMinutes()));
                ts.setQueueNumber(i + 1);
                start = start.plusMinutes(request.getNewDurationInMinutes());
            }

            timeSlotRepository.saveAll(patientTimeSlots);
        }
    }

    @Override
    public void bulkDeleteSlots(BulkDeleteSlotsRequest request) {
        List<AppointmentSlot> slots = appointmentSlotRepository
                .findAllByDoctorIdAndDateBetween(request.getDoctorId(), request.getStartDate(), request.getEndDate());

        for (AppointmentSlot slot : slots) {
            List<PatientTimeSlot> patientTimeSlots = timeSlotRepository.findByAppointmentSlotOrderByQueueNumberAsc(slot);
            timeSlotRepository.deleteAll(patientTimeSlots);
            appointmentSlotRepository.delete(slot);
        }
    }

    @Override
    public void importSlotsFromCsv(MultipartFile file) throws IOException {
        List<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream()))
                .lines()
                .skip(1)
                .toList();

        for (String line : lines) {
            String[] fields = line.split(",");
            String doctorId = String.valueOf(fields[0]);
            LocalDate date = LocalDate.parse(fields[1]);
            LocalTime start = LocalTime.parse(fields[2]);
            LocalTime end = LocalTime.parse(fields[3]);
            SlotType slotType = SlotType.valueOf(fields[4]);
            int duration = Integer.parseInt(fields[5]);

            createSlot(SlotCreateRequest.builder()
                    .doctorId(doctorId)
                    .date(date)
                    .startTime(start)
                    .endTime(end)
                    .slotDurationInMinutes(duration)
                    .slotType(slotType)
                    .build());
        }
    }
}
