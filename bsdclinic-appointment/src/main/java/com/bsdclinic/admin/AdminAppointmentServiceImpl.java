package com.bsdclinic.admin;

import com.bsdclinic.AppointmentMapper;
import com.bsdclinic.AppointmentRepository;
import com.bsdclinic.MedicalRecordRepoForAppointment;
import com.bsdclinic.appointment.ActionStatus;
import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.appointment.Appointment_;
import com.bsdclinic.dto.AppointmentDto;
import com.bsdclinic.dto.request.AppointmentFilter;
import com.bsdclinic.dto.request.AppointmentUpdate;
import com.bsdclinic.dto.response.AppointmentResponse;
import com.bsdclinic.dto.response.IAppointmentStatusCount;
import com.bsdclinic.dto.response.StatusTransitionResponse;
import com.bsdclinic.exception_handler.exception.BadRequestException;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.status_flow.ActionStatusFlow;
import com.bsdclinic.subscriber.Subscriber;
import com.bsdclinic.subscriber.SubscriberRepository;
import com.bsdclinic.subscriber.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAppointmentServiceImpl implements AdminAppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final SubscriberRepository subscriberRepository;
    private final AppointmentMapper appointmentMapper;
    private final MessageProvider messageProvider;
    private final SubscriberService subscriberService;
    private final ActionStatusFlow actionStatusFlow;
    private final MedicalRecordRepoForAppointment medicalRecordRepository;

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

    @Override
    public Map<String, Integer> getAppointmentStatusCount() {
        Map<String, Integer> appointmentStatusCount = appointmentRepository.getAppointmentStatusCount()
                .stream()
                .collect(Collectors.toMap(
                        IAppointmentStatusCount::getActionStatus,
                        IAppointmentStatusCount::getStatusCount
                ));

        return Arrays.stream(ActionStatus.values())
                .map(Enum::name)
                .collect(Collectors.toMap(
                        actionStatus -> actionStatus,
                        actionStatus -> appointmentStatusCount.getOrDefault(actionStatus, 0)
                ));
    }

    @Override
    public void updateAppointment(String appointmentId, AppointmentUpdate appointmentUpdate) {
        Appointment appointment = getAppointmentById(appointmentId);
        ActionStatus currentStatus = ActionStatus.valueOf(appointment.getActionStatus());
        ActionStatus nextStatus = ActionStatus.valueOf(appointmentUpdate.getActionStatus());
        if (!actionStatusFlow.canTransition(currentStatus, nextStatus)) {
            throw new BadRequestException(
                    Map.of(Appointment_.ACTION_STATUS, messageProvider.getMessage("validation.invalid.appointment_status"))
            );
        }

        if (!actionStatusFlow.isRoleAllowed(currentStatus, nextStatus, appointmentUpdate.getUserRoleCode())) {
            throw new AccessDeniedException(messageProvider.getMessage("error.403.operate"));
        }

        appointment = appointmentMapper.toAppointment(appointmentUpdate, appointment);
        appointmentRepository.save(appointment);
    }

    private Appointment getAppointmentById(String appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new NotFoundException(messageProvider.getMessage("validation.no_exist.appointment")));
    }

    @Override
    public StatusTransitionResponse getNextStatus(String appointmentId, String role) {
        Appointment appointment = getAppointmentById(appointmentId);
        ActionStatus currentStatus = ActionStatus.valueOf(appointment.getActionStatus());
        Set<ActionStatus> next = actionStatusFlow.getNextStatesByRole(currentStatus, role);
        return new StatusTransitionResponse(currentStatus, next);
    }

    @Override
    public DatatableResponse getAppointmentsForDoctor(AppointmentFilter appointmentFilter) {
        DatatableResponse<AppointmentResponse> appointments = getAppointmentsByFilter(appointmentFilter);
        List<AppointmentResponse> appointmentResponses = appointments.getData();
        List<String> appointmentIds = appointmentResponses.stream().map(AppointmentResponse::getAppointmentId).toList();
        Map<String, String> medicalRecordIdsMap = medicalRecordRepository.findMedicalRecordIdsAsMap(appointmentIds);
        appointmentResponses.forEach(appointmentResponse -> {
            String medicalRecordId = medicalRecordIdsMap.get(appointmentResponse.getAppointmentId());
            appointmentResponse.setMedicalRecordId(medicalRecordId);
        });
        appointments.setData(appointmentResponses);

        return appointments;
    }

    @Override
    public boolean existsAppointment(String appointmentId) {
        return appointmentRepository.existsById(appointmentId);
    }

    @Override
    public AppointmentResponse getAppointment(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException(messageProvider.getMessage("validation.no_exist.appointment")));
        return appointmentMapper.toAppointmentResponse(appointment);
    }
}
