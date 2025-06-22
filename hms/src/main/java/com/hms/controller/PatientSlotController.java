package com.hms.controller;

import com.hms.dto.*;
import com.hms.service.PatientSlotService;
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
public class PatientSlotController {

    private final PatientSlotService patientSlotService;

    @PostMapping()
    public ResponseEntity<String> createSlot(@RequestBody SlotCreateRequest request) {
        String result = patientSlotService.createSlot(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/recurring")
    public ResponseEntity<String> createRecurringSlots(@RequestBody RecurringSlotRequest request) {
        String result = patientSlotService.createRecurringSlots(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookSlot(@RequestBody SlotBookingRequest request) {
        String result = patientSlotService.bookSlot(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableTimeSlotResponse>> getAvailableSlots(
            @RequestParam String doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(patientSlotService.getAvailableTimeSlots(doctorId, date));
    }

    @PutMapping("/{appointmentSlotId}")
    public ResponseEntity<String> updateSlot(
            @PathVariable String appointmentSlotId,
            @RequestBody SlotUpdateRequest request
    ) {
        patientSlotService.updateAppointmentSlot(appointmentSlotId, request);
        return ResponseEntity.ok("Slot updated successfully");
    }

    @DeleteMapping("/{appointmentSlotId}")
    public ResponseEntity<String> deleteSlot(@PathVariable String appointmentSlotId) {
        patientSlotService.deleteSlotWithTimeSlots(appointmentSlotId);
        return ResponseEntity.ok("Slot deleted successfully");
    }

    @PostMapping("/bulk-update-duration")
    public ResponseEntity<String> bulkUpdateSlotDurations(@RequestBody BulkUpdateSlotDurationRequest request) {
        patientSlotService.bulkUpdateSlotDurations(request);
        return ResponseEntity.ok("Updated all slots successfully");
    }

    @DeleteMapping("/bulk-delete")
    public ResponseEntity<String> bulkDeleteSlots(@RequestBody BulkDeleteSlotsRequest request) {
        patientSlotService.bulkDeleteSlots(request);
        return ResponseEntity.ok("All relevant slots deleted");
    }

    @PostMapping("/import-csv")
    public ResponseEntity<String> importSlots(@RequestParam("file") MultipartFile file) throws IOException {
        patientSlotService.importSlotsFromCsv(file);
        return ResponseEntity.ok("Imported successfully");
    }

}
