package com.hms.controller;

import com.hms.dto.RecurringSlotRequest;
import com.hms.dto.SlotBookingRequest;
import com.hms.dto.SlotCreateRequest;
import com.hms.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class SlotController {

    private final SlotService slotService;

    @PostMapping()
    public ResponseEntity<String> createSlot(@RequestBody SlotCreateRequest request) {
        String result = slotService.createSlot(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/recurring")
    public ResponseEntity<String> createRecurringSlots(@RequestBody RecurringSlotRequest request) {
        String result = slotService.createRecurringSlots(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookSlot(@RequestBody SlotBookingRequest request) {
        String result = slotService.bookSlot(request);
        return ResponseEntity.ok(result);
    }


}
