package com.hms.service;

import com.hms.model.Patient;

public interface AppointmentService {
    Patient assignPatientToNextAvailableSlot(Long doctorId, String patientName);
    String markPatientPresent(Long patientId);
}
