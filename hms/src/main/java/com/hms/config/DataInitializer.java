package com.hms.config;

import com.hms.model.Doctor;
import com.hms.model.TimeSlot;
import com.hms.repository.AppointmentSlotRepository;
import com.hms.repository.DoctorRepository;
import com.hms.repository.TimeSlotRepository;
import com.hms.util.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final AppointmentSlotRepository appointmentSlotRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Doctor> doctors = List.of(
                new Doctor(getId(), "Dr. A", "General Physician"),
                new Doctor(getId(), "Dr. B", "General Physician"),
                new Doctor(getId(), "Dr. C", "General Physician")
        );

        LocalTime[] startTimes = { LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 0) };

        for (int i = 0; i < doctors.size(); i++) {
            Doctor doctor = doctors.get(i);
            doctorRepository.save(doctor);

            for (int j = 0; j < 4; j++) {
                TimeSlot ts = new TimeSlot();
                ts.setId(Utility.generateId());
                ts.setQueueNumber(i+1);
                ts.setStartTime(startTimes[i].plusMinutes(j * 15));
                ts.setEndTime(startTimes[i].plusMinutes((j + 1) * 15));
                timeSlotRepository.save(ts);
            }
        }
    }

    String getId() {
        return Utility.generateId(Utility.DOCTOR);
    }
}
