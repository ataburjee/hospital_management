package com.hms.service;

import com.hms.model.Patient;

public interface AppointmentService {
    Patient assignPatientToNextAvailableSlot(String doctorId, String patientName);
    String markPatientPresent(String patientId);
}
