package com.hms.repository;

import com.hms.model.AppointmentSlot;
import com.hms.model.SlotStatus;
import com.hms.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findByAppointmentSlotAndSlotStatusOrderByQueueNumber(AppointmentSlot slot, SlotStatus status);

    Optional<TimeSlot> findFirstByAppointmentSlotAndQueueNumberGreaterThanAndSlotStatusOrderByQueueNumberAsc(
            AppointmentSlot slot,
            int queueNumber,
            SlotStatus status
    );

    Optional<TimeSlot> findByAppointmentSlotAndQueueNumber(AppointmentSlot slot, int current);

    Optional<TimeSlot> findFirstByAppointmentSlotAndSlotStatusOrderByQueueNumberAsc(
            AppointmentSlot slot,
            SlotStatus slotStatus
    );

}