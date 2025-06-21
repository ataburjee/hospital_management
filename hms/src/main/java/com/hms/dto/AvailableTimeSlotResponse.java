package com.hms.dto;

import com.hms.enums.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AvailableTimeSlotResponse {
    private String timeSlotId;
    private String startTime;
    private String endTime;
    private SlotStatus status;
}

