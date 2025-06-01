package com.bsdclinic;

import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.dto.AppointmentDto;
import com.bsdclinic.subscriber.Subscriber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentMapper {

    @Mapping(target = "email", source = "subscriberEmail")
    @Mapping(target = "name", source = "subscriberFullName")
    @Mapping(target = "phone", source = "subscriberPhone")
    Subscriber toSubscriber(AppointmentDto request);

    Appointment toAppointment(AppointmentDto request);
}
