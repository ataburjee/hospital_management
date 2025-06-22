package com.hms.dto;

import com.hms.enums.SlotType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
public class SlotCreateRequest {

    private String doctorId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDurationInMinutes;
    private SlotType slotType;
    private String tag;
    private String note;
    private String colorCode;
    private String recurrencePattern;

}