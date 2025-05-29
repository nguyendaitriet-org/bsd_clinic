package com.bsdclinic.client;

import com.bsdclinic.AppointmentRepository;
import com.bsdclinic.ClinicInfoDto;
import com.bsdclinic.ClinicInfoService;
import com.bsdclinic.client.response.AvailableAppointmentSlot;
import com.bsdclinic.clinic_info.ClinicInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClientAppointmentServiceImpl implements ClientAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final ClinicInfoService clinicInfoService;

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public AvailableAppointmentSlot getAvailableSlots(LocalDate registerDate) {
        ClinicInfoDto clinicInfoDto = clinicInfoService.getClinicInfo();
        Map<String, List<ClinicInfo.TimeRange>> workingSchedule = clinicInfoDto.getWorkingHours();
        List<String> allTimeSlots = generateTimeSlots(registerDate, clinicInfoDto.getRegisterTimeRange(), workingSchedule);
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

    private List<String> generateTimeSlots(
            LocalDate date,
            Integer intervalMinutes,
            Map<String, List<ClinicInfo.TimeRange>> workingSchedule
    ) {
        DayOfWeek day = date.getDayOfWeek();
        List<ClinicInfo.TimeRange> workingHours = workingSchedule.get(day.name());
        if (workingHours == null) return List.of();

        List<String> slots = new ArrayList<>();
        LocalTime nowPlusOneHour = null;

        if (date.equals(LocalDate.now())) {
            nowPlusOneHour = LocalTime.now().plusHours(1);
        }

        for (ClinicInfo.TimeRange range : workingHours) {
            LocalTime current = range.start();
            while (!current.plusMinutes(intervalMinutes).isAfter(range.end())) {
                if (nowPlusOneHour == null || current.isAfter(nowPlusOneHour)) {
                    slots.add(current.format(TIME_FORMATTER));
                }
                current = current.plusMinutes(intervalMinutes);
            }
        }
        return slots;
    }
}
