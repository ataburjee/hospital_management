package com.hms.controller;

import com.hms.model.Patient;
import com.hms.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/auto-assign")
    public ResponseEntity<Patient> assignPatient(
            @RequestParam String doctorId,
            @RequestParam String name)
    {
        Patient patient = appointmentService.assignPatientToNextAvailableSlot(doctorId, name);
        return ResponseEntity.ok(patient);
    }

    @PostMapping("/present")
    public ResponseEntity<String> markPresent(@RequestParam String patientId) {
        String result = appointmentService.markPatientPresent(patientId);
        return ResponseEntity.ok(result);
    }
}
