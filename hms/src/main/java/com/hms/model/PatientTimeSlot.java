package com.hms.model;

import com.hms.enums.SlotStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Data
@Table(name = "time_slot")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientTimeSlot {
    @Id
    private String id;

    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_slot_id")
    private AppointmentSlot appointmentSlot;

    @Enumerated(EnumType.STRING)
    private SlotStatus slotStatus;

    @OneToOne(mappedBy = "patientTimeSlot")
    private Patient patient;

    private int queueNumber;

}