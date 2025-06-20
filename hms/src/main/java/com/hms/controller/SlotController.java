package com.hms.controller;

import com.hms.dto.*;
import com.hms.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/available")
    public ResponseEntity<List<AvailableTimeSlotResponse>> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(slotService.getAvailableTimeSlots(doctorId, date));
    }

    @PutMapping("/{appointmentSlotId}")
    public ResponseEntity<String> updateSlot(
            @PathVariable Long appointmentSlotId,
            @RequestBody SlotUpdateRequest request
    ) {
        slotService.updateAppointmentSlot(appointmentSlotId, request);
        return ResponseEntity.ok("Slot updated successfully");
    }

    @DeleteMapping("/{appointmentSlotId}")
    public ResponseEntity<String> deleteSlot(@PathVariable Long appointmentSlotId) {
        slotService.deleteSlotWithTimeSlots(appointmentSlotId);
        return ResponseEntity.ok("Slot deleted successfully");
    }

    @PostMapping("/bulk-update-duration")
    public ResponseEntity<String> bulkUpdateSlotDurations(@RequestBody BulkUpdateSlotDurationRequest request) {
        slotService.bulkUpdateSlotDurations(request);
        return ResponseEntity.ok("Updated all slots successfully");
    }
    @DeleteMapping("/bulk-delete")
    public ResponseEntity<String> bulkDeleteSlots(@RequestBody BulkDeleteSlotsRequest request) {
        slotService.bulkDeleteSlots(request);
        return ResponseEntity.ok("All relevant slots deleted");
    }

    @PostMapping("/import-csv")
    public ResponseEntity<String> importSlots(@RequestParam("file") MultipartFile file) throws IOException {
        slotService.importSlotsFromCsv(file);
        return ResponseEntity.ok("Imported successfully");
    }

}
