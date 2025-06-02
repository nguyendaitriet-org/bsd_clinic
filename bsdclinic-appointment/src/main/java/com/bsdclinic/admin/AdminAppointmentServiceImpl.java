package com.bsdclinic.admin;

import com.bsdclinic.AppointmentMapper;
import com.bsdclinic.AppointmentRepository;
import com.bsdclinic.appointment.ActionStatus;
import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.dto.AppointmentDto;
import com.bsdclinic.subscriber.Subscriber;
import com.bsdclinic.subscriber.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAppointmentServiceImpl implements AdminAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final SubscriberRepository subscriberRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public void createNewAppointment(AppointmentDto appointmentDto) {
        Subscriber subscriber = subscriberRepository.findByPhone(appointmentDto.getSubscriberPhone());
        if (subscriber == null) {
            Subscriber newSubscriber = appointmentMapper.toSubscriber(appointmentDto);
            subscriber = subscriberRepository.save(newSubscriber);
        }

        Appointment newAppointment = appointmentMapper.toAppointment(appointmentDto);
        newAppointment.setSubscriberId(subscriber.getSubscriberId());
        newAppointment.setActionStatus(ActionStatus.CHECKED_IN.name());

        appointmentRepository.save(newAppointment);
    }
}
