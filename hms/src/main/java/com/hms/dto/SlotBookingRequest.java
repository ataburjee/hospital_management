package com.hms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotBookingRequest {
    private Long patientId;
    private Long appointmentSlotId;
    private String patientName;
}

