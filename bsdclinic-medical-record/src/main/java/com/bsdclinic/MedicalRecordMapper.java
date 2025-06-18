package com.bsdclinic;

import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.dto.request.AppointmentUpdate;
import com.bsdclinic.dto.request.MedicalRecordUpdateRequest;
import com.bsdclinic.dto.response.MedicalRecordResponse;
import com.bsdclinic.medical_record.MedicalRecord;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicalRecordMapper {
    MedicalRecordResponse toDto(MedicalRecord medicalRecord);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MedicalRecord toEntity(MedicalRecordUpdateRequest request, @MappingTarget MedicalRecord medicalRecord);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Appointment toAppointment(MedicalRecordUpdateRequest request, @MappingTarget Appointment appointment);
}
