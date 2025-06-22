package com.hms.repository;

import com.hms.enums.SlotStatus;
import com.hms.model.AppointmentSlot;
import com.hms.model.PatientTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<PatientTimeSlot, String> {

    List<PatientTimeSlot> findByAppointmentSlotAndSlotStatusOrderByQueueNumber(AppointmentSlot slot, SlotStatus status);

    Optional<PatientTimeSlot> findFirstByAppointmentSlotAndQueueNumberGreaterThanAndSlotStatusOrderByQueueNumberAsc(
            AppointmentSlot slot,
            int queueNumber,
            SlotStatus status
    );

    Optional<PatientTimeSlot> findByAppointmentSlotAndQueueNumber(AppointmentSlot slot, int current);

    Optional<PatientTimeSlot> findFirstByAppointmentSlotAndSlotStatusOrderByQueueNumberAsc(
            AppointmentSlot slot,
            SlotStatus slotStatus
    );

    List<PatientTimeSlot> findByAppointmentSlotOrderByQueueNumberAsc(AppointmentSlot slot);

    Optional<List<PatientTimeSlot>> findByAppointmentSlot(AppointmentSlot slot);
}