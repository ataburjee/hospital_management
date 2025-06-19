package com.hms.service.impl;

import com.hms.model.*;
import com.hms.repository.AppointmentSlotRepository;
import com.hms.repository.DoctorRepository;
import com.hms.repository.PatientRepository;
import com.hms.repository.TimeSlotRepository;
import com.hms.service.AppointmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Transactional
    public Patient assignPatientToNextAvailableSlot(Long doctorId, String patientName) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Traversing doctor’s time slots to find next availability slot
        for (AppointmentSlot slot : doctor.getSlots()) {
            for (TimeSlot timeSlot : slot.getTimeSlots()) {
                if (timeSlot.getPatient() == null) {
                    Patient patient = new Patient();
                    patient.setName(patientName);
                    patient.setTimeSlot(timeSlot);
                    patient.setPresent(false);
                    patientRepository.save(patient);

                    timeSlot.setPatient(patient);
                    timeSlotRepository.save(timeSlot);

                    return patient;
                }
            }
        }
        throw new RuntimeException("No slots available");
    }

    @Transactional
    public String markPatientPresent(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setPresent(true);
        patientRepository.save(patient);

        return "Patient marked present: " + patient.getName();
    }

}

