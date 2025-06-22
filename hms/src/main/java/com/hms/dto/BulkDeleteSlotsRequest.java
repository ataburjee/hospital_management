package com.hms.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BulkDeleteSlotsRequest {
    private String doctorId;
    private LocalDate startDate;
    private LocalDate endDate;
}
