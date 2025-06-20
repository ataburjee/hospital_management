package com.hms.service;

import com.hms.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface SlotService {
    String createSlot(SlotCreateRequest request);
    String createRecurringSlots(RecurringSlotRequest request);
    String bookSlot(SlotBookingRequest request);
    List<AvailableTimeSlotResponse> getAvailableTimeSlots(Long doctorId, LocalDate date);

    void updateAppointmentSlot(Long appointmentSlotId, SlotUpdateRequest request);

    void deleteSlotWithTimeSlots(Long appointmentSlotId);

    void bulkUpdateSlotDurations(BulkUpdateSlotDurationRequest request);

    void bulkDeleteSlots(BulkDeleteSlotsRequest request);

    void importSlotsFromCsv(MultipartFile file) throws IOException;
}
