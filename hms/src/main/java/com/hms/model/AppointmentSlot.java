package com.hms.model;

import com.hms.enums.SlotStatus;
import com.hms.enums.SlotType;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;

import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "appointment_slot", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"doctor_id", "slot_date", "start_time", "end_time"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSlot {

    @Id
    private String id;

    @Column(name = "slot_date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "slot_type")
    private SlotType slotType;

    @Enumerated(EnumType.STRING)
    @Column(name = "slot_status")
    private SlotStatus slotStatus;

    @Column(name = "tag")
    private String tag;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "recurrence_pattern")
    private String recurrencePattern;

    @Version
    private Long version;

    @Column(name = "current_queue")
    private Integer currentQueueNumber = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToMany(mappedBy = "appointmentSlot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientTimeSlot> patientTimeSlots;

}