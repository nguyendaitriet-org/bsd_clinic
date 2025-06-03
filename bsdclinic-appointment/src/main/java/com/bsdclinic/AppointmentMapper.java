package com.bsdclinic;

import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.dto.AppointmentDto;
import com.bsdclinic.subscriber.Subscriber;
import com.bsdclinic.subscriber.SubscriberDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

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
}
