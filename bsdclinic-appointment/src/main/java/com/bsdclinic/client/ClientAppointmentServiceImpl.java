package com.bsdclinic.client;

import com.bsdclinic.AppointmentMapper;
import com.bsdclinic.AppointmentRepository;
import com.bsdclinic.ClinicInfoDto;
import com.bsdclinic.ClinicInfoService;
import com.bsdclinic.appointment.ActionStatus;
import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.client.response.AvailableAppointmentSlot;
import com.bsdclinic.clinic_info.ClinicInfo;
import com.bsdclinic.constant.DateTimePattern;
import com.bsdclinic.dto.AppointmentDto;
import com.bsdclinic.exception_handler.exception.BadRequestException;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.subscriber.Subscriber;
import com.bsdclinic.subscriber.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final SubscriberRepository subscriberRepository;
    private final AppointmentMapper appointmentMapper;
    private final MessageProvider messageProvider;

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
                    slots.add(current.format(DateTimeFormatter.ofPattern(DateTimePattern.HOUR_MINUTE)));
                }
                current = current.plusMinutes(intervalMinutes);
            }
        }

        return slots;
    }

    @Override
    public void createNewAppointment(AppointmentDto appointmentDto) {
        Subscriber subscriber = subscriberRepository.findByPhone(appointmentDto.getSubscriberPhone());

        if (appointmentRepository.existsByRegisterDateAndRegisterTime(
                LocalDate.parse(appointmentDto.getRegisterDate()),
                appointmentDto.getRegisterTime()
        )) {
            Map<String, String> errors = Map.of("registerTime", messageProvider.getMessage("validation.picked.register_time"));
            throw new BadRequestException(errors);
        }

        if (subscriber == null) {
            Subscriber newSubscriber = appointmentMapper.toSubscriber(appointmentDto);
            subscriber = subscriberRepository.save(newSubscriber);
        }

        Appointment newAppointment = appointmentMapper.toAppointment(appointmentDto);
        newAppointment.setSubscriberId(subscriber.getSubscriberId());
        newAppointment.setActionStatus(ActionStatus.PENDING.name());

        appointmentRepository.save(newAppointment);
    }
}
