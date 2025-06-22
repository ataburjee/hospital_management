package com.hms.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class BulkUpdateSlotDurationRequest {
    private String doctorId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int newDurationInMinutes;
}

