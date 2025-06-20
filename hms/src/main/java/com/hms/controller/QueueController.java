package com.hms.controller;

import com.hms.dto.QueueStatusResponse;
import com.hms.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @PostMapping("/mark-arrived/{appointmentSlotId}")
    public ResponseEntity<String> markQueueStarted(@PathVariable("appointmentSlotId") Long appointmentSlotId) {
        return ResponseEntity.ok(queueService.markQueueStarted(appointmentSlotId));
    }

    @PostMapping("/mark-missed/{appointmentSlotId}")
    public ResponseEntity<String> markMissed(@PathVariable("appointmentSlotId") Long appointmentSlotId) {
        return ResponseEntity.ok(queueService.markCurrentAsMissed(appointmentSlotId));
    }

    @PostMapping("/arrive-late")
    public ResponseEntity<String> arriveLate(
            @RequestParam("appointmentSlotId") Long appointmentSlotId,
            @RequestParam("missedQueueNumber") int missedQueueNumber
    ) {
        return ResponseEntity.ok(queueService.markLateArrival(appointmentSlotId, missedQueueNumber));
    }

    @PostMapping("/status/{appointmentSlotId}")
    public ResponseEntity<List<QueueStatusResponse>> getQueueStatus(@PathVariable("appointmentSlotId") Long appointmentSlotId) {
        return ResponseEntity.ok(queueService.getQueueStatus(appointmentSlotId));
    }

    @PostMapping("/complete-current/{appointmentSlotId}")
    public ResponseEntity<String> completeAndPromote(@PathVariable Long appointmentSlotId) {
        return ResponseEntity.ok(queueService.completeCurrentAndPromoteNext(appointmentSlotId));
    }
}
