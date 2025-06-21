package com.hms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotBookingRequest {
    private String patientId;
    private String appointmentSlotId;
    private String patientName;
}

