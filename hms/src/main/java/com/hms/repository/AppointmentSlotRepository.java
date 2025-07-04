package com.hms.repository;

import com.hms.model.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, String> {

    @Query("""
        SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
        FROM AppointmentSlot s
        WHERE s.doctor.id = :doctorId
          AND s.date = :date
          AND (
              (s.startTime < :endTime AND s.endTime > :startTime)
          )
    """)
    boolean existsByDoctorIdAndDateAndTimeRange(
            @Param("doctorId") String doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    List<AppointmentSlot> findByDoctorIdAndDate(String doctorId, LocalDate date);

    List<AppointmentSlot> findAllByDoctorIdAndDateBetween(
            String doctorId,
            LocalDate fromDate,
            LocalDate toDate
    );
}

