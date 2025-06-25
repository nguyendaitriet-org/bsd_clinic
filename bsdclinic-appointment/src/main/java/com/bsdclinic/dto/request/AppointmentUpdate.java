package com.bsdclinic.dto.request;

import com.bsdclinic.validation.AppointmentRuleAnnotation;
import com.bsdclinic.validation.group.OnAdminCreate;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentUpdate {
    @AppointmentRuleAnnotation.ValidAppointmentStatus(message = "{validation.invalid.appointment_status}")
    private String actionStatus;

    @NotBlank(message = "{validation.required.doctor_id}", groups = OnAdminCreate.class)
    @AppointmentRuleAnnotation.ValidDoctorId
    private String doctorId;

    private String userRoleCode;
}
