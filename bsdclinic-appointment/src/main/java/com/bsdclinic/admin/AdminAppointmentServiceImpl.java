package com.bsdclinic.admin;

import com.bsdclinic.AppointmentMapper;
import com.bsdclinic.AppointmentRepository;
import com.bsdclinic.appointment.ActionStatus;
import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.dto.AppointmentDto;
import com.bsdclinic.dto.request.AppointmentFilter;
import com.bsdclinic.dto.response.AppointmentResponse;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.subscriber.Subscriber;
import com.bsdclinic.subscriber.SubscriberRepository;
import com.bsdclinic.subscriber.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAppointmentServiceImpl implements AdminAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final SubscriberRepository subscriberRepository;
    private final AppointmentMapper appointmentMapper;
    private final MessageProvider messageProvider;
    private final SubscriberService subscriberService;

    @Override
    public void createNewAppointment(AppointmentDto appointmentDto) {
        Subscriber subscriber = subscriberRepository.findByPhone(appointmentDto.getSubscriberPhone());
        if (subscriber == null) {
            subscriberService.checkDuplicateSubscriberEmail(appointmentDto.getSubscriberEmail());
            Subscriber newSubscriber = appointmentMapper.toSubscriber(appointmentDto);
            subscriber = subscriberRepository.save(newSubscriber);
        }

        Appointment newAppointment = appointmentMapper.toAppointment(appointmentDto);
        newAppointment.setRegisterDate(LocalDate.now());
        newAppointment.setSubscriberId(subscriber.getSubscriberId());
        newAppointment.setActionStatus(ActionStatus.CHECKED_IN.name());

        appointmentRepository.save(newAppointment);
    }

    @Override
    public DatatableResponse getAppointmentsByFilter(AppointmentFilter appointmentFilter) {
        String subscriberId = appointmentFilter.getSubscriberId();
        if (StringUtils.isNotBlank(subscriberId) && !subscriberRepository.existsBySubscriberId(subscriberId)) {
            throw new NotFoundException(messageProvider.getMessage("validation.no_exist.subscriber"));
        }
        Specification<Appointment> appointmentSpecification = AppointmentSpecifications.withFilter(appointmentFilter);
        Pageable pageable = PageRequest.of(
                appointmentFilter.getStart() / appointmentFilter.getLength(),
                appointmentFilter.getLength()
        );
        Page<Appointment> appointments = appointmentRepository.findAll(appointmentSpecification, pageable);

        List<AppointmentResponse> appointmentResponse = appointments.stream()
                .map(appointmentMapper::toAppointmentResponse).toList();

        DatatableResponse<AppointmentResponse> datatableResponse = new DatatableResponse<>();
        datatableResponse.setData(appointmentResponse);
        datatableResponse.setDraw(appointmentFilter.getDraw());
        Long totalRecord = appointments.getTotalElements();
        datatableResponse.setRecordsFiltered(totalRecord);
        datatableResponse.setRecordsTotal(totalRecord);

        return datatableResponse;
    }
}
