package com.hms.service.impl;

import com.hms.model.AppointmentSlot;
import com.hms.model.SlotStatus;
import com.hms.model.TimeSlot;
import com.hms.repository.AppointmentSlotRepository;
import com.hms.repository.TimeSlotRepository;
import com.hms.service.QueueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private final AppointmentSlotRepository appointmentSlotRepository;
    private final TimeSlotRepository timeSlotRepository;


    @Override
    @Transactional
    public String markQueueStarted(Long appointmentSlotId) {
        AppointmentSlot slot = appointmentSlotRepository.findById(appointmentSlotId)
                .orElseThrow(() -> new RuntimeException("AppointmentSlot not found"));

        Optional<TimeSlot> firstBooked = timeSlotRepository
                .findFirstByAppointmentSlotAndSlotStatusOrderByQueueNumberAsc(slot, SlotStatus.BOOKED);

        if (firstBooked.isPresent()) {
            TimeSlot slotToMark = firstBooked.get();
            slotToMark.setSlotStatus(SlotStatus.CURRENT);
            slot.setCurrentQueueNumber(slotToMark.getQueueNumber());

            appointmentSlotRepository.save(slot);
            timeSlotRepository.save(slotToMark);

            return "Queue started. Patient #" + slotToMark.getQueueNumber() + " is now CURRENT.";
        }

        return "No booked patients to mark as CURRENT.";
    }

    @Override
    @Transactional
    public String markCurrentAsMissed(Long appointmentSlotId) {
        AppointmentSlot slot = appointmentSlotRepository.findById(appointmentSlotId)
                .orElseThrow(() -> new RuntimeException("AppointmentSlot not found"));

        int current = slot.getCurrentQueueNumber();

        TimeSlot currentSlot = timeSlotRepository.findByAppointmentSlotAndQueueNumber(slot, current)
                .orElseThrow(() -> new RuntimeException("Current time slot not found"));

        currentSlot.setSlotStatus(SlotStatus.MISSED);
        timeSlotRepository.save(currentSlot);

        Optional<TimeSlot> nextSlotOpt = timeSlotRepository
                .findFirstByAppointmentSlotAndQueueNumberGreaterThanAndSlotStatusOrderByQueueNumberAsc(
                        slot, current, SlotStatus.BOOKED
                );

        if (nextSlotOpt.isPresent()) {
            TimeSlot next = nextSlotOpt.get();
            next.setSlotStatus(SlotStatus.CURRENT);
            slot.setCurrentQueueNumber(next.getQueueNumber());
            appointmentSlotRepository.save(slot);
            timeSlotRepository.save(next);
            return "Marked queue #" + current + " as MISSED. Promoted queue #" + next.getQueueNumber() + " to CURRENT.";
        }

        return "No booked patients left to promote.";
    }


    @Override
    @Transactional
    public String markLateArrival(Long appointmentSlotId, int missedQueueNumber) {
        AppointmentSlot slot = appointmentSlotRepository.findById(appointmentSlotId)
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        int current = slot.getCurrentQueueNumber();

        TimeSlot missedSlot = timeSlotRepository.findByAppointmentSlotAndQueueNumber(slot, missedQueueNumber)
                .orElseThrow(() -> new RuntimeException("Invalid missed queue number"));

        if (missedSlot.getSlotStatus() != SlotStatus.MISSED) {
            return "This slot was not marked as MISSED.";
        }

        missedSlot.setSlotStatus(SlotStatus.BOOKED);
        timeSlotRepository.save(missedSlot);

        return "Late patient (queue #" + missedQueueNumber + ") re-added. Will be called after current patient (#" + current + ").";
    }

}
