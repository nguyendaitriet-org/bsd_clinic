package com.bsdclinic.client;

import com.bsdclinic.AppointmentRepository;
import com.bsdclinic.client.constant.ClinicSchedule;
import com.bsdclinic.client.response.AvailableAppointmentSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientAppointmentServiceImpl implements ClientAppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Override
    public AvailableAppointmentSlot getAvailableSlots(LocalDate registerDate) {
        List<String> allTimeSlots = generateTimeSlots(registerDate, 15);
        List<String> reservedTimes = appointmentRepository.findRegisterTimesByRegisterDay(registerDate);
        List<String> availableTimeSlots = allTimeSlots.stream()
                .filter(time -> !reservedTimes.contains(time))
                .toList();

        return AvailableAppointmentSlot.builder()
                .registerDate(registerDate)
                .day(registerDate.getDayOfWeek().toString())
                .availableSlots(availableTimeSlots)
                .build();
    }

    public static List<String> generateTimeSlots(LocalDate date, int intervalMinutes) {
        DayOfWeek day = date.getDayOfWeek();
        List<ClinicSchedule.TimeRange> workingHours = ClinicSchedule.CLINIC_SCHEDULE.get(day);
        if (workingHours == null) return List.of();

        List<String> slots = new ArrayList<>();
        for (ClinicSchedule.TimeRange range : workingHours) {
            LocalTime current = range.start();
            while (!current.plusMinutes(intervalMinutes).isAfter(range.end())) {
                slots.add(current.format(ClinicSchedule.TIME_FORMATTER));
                current = current.plusMinutes(intervalMinutes);
            }
        }
        return slots;
    }
}
