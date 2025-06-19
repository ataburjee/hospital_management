package com.hms.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class BulkUpdateSlotDurationRequest {
    private Long doctorId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int newDurationInMinutes; // e.g., 30
}

