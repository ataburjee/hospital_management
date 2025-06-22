package com.hms.service.impl;

import com.hms.model.AppointmentSlot;
import com.hms.model.Doctor;
import com.hms.model.Patient;
import com.hms.model.PatientTimeSlot;
import com.hms.repository.DoctorRepository;
import com.hms.repository.PatientRepository;
import com.hms.service.AppointmentService;
import com.hms.util.Utility;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public Patient assignPatientToNextAvailableSlot(String doctorId, String patientName) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Traversing doctor’s time slots to find next availability slot
        for (AppointmentSlot slot : doctor.getSlots()) {
            for (PatientTimeSlot patientTimeSlot : slot.getPatientTimeSlots()) {
                if (patientTimeSlot.getPatient() == null) {
                    return Patient.builder()
                            .id(Utility.generateId(Utility.PATIENT))
                            .name(patientName)
                            .patientTimeSlot(patientTimeSlot)
                            .isPresent(false)
                            .build();
//                    patient.setName(patientName);
//                    patient.setTimeSlot(timeSlot);
//                    patient.setPresent(false);
//                    patientRepository.save(patient);
//
//                    timeSlot.setPatient(patient);
//                    timeSlotRepository.save(timeSlot);

//                    return patient;
                }
            }
        }
        throw new RuntimeException("No slots available");
    }

    @Transactional
    public String markPatientPresent(String patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setPresent(true);
        patientRepository.save(patient);

        return "Patient marked present: " + patient.getName();
    }

}

