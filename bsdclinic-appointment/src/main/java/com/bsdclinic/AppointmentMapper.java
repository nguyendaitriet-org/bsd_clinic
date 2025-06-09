package com.bsdclinic;

import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.dto.AppointmentDto;
import com.bsdclinic.dto.request.AppointmentUpdate;
import com.bsdclinic.dto.response.AppointmentResponse;
import com.bsdclinic.subscriber.Subscriber;
import com.bsdclinic.subscriber.SubscriberDto;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {
    @Mapping(target = "email", source = "subscriberEmail")
    @Mapping(target = "name", source = "subscriberName")
    @Mapping(target = "phone", source = "subscriberPhone")
    Subscriber toSubscriber(AppointmentDto appointmentDto);

    Appointment toAppointment(AppointmentDto appointmentDto);

    @Mapping(target = "subscriberEmail", source = "email")
    @Mapping(target = "subscriberName", source = "name")
    @Mapping(target = "subscriberPhone", source = "phone")
    SubscriberDto toSubscriberDto(Subscriber subscriber);

    AppointmentResponse toAppointmentResponse(Appointment appointment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Appointment toAppointment(AppointmentUpdate request, @MappingTarget Appointment appointment);
}
