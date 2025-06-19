package com.hms.dto;

import com.hms.enums.SlotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueueStatusResponse {
    private int queueNumber;
    private String patientName;
    private SlotStatus slotStatus;
}