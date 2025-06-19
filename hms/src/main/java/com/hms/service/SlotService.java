package com.hms.service;

import com.hms.dto.RecurringSlotRequest;
import com.hms.dto.SlotBookingRequest;
import com.hms.dto.SlotCreateRequest;

public interface SlotService {
    String createSlot(SlotCreateRequest request);
    String createRecurringSlots(RecurringSlotRequest request);
    String bookSlot(SlotBookingRequest request);
}
