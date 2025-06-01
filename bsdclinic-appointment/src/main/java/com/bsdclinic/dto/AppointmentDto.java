package com.bsdclinic.dto;

import com.bsdclinic.validation.AppointmentRuleAnnotation;
import com.bsdclinic.validation.ValidationConstant;
import com.bsdclinic.validation.group.OnAdminCreate;
import com.bsdclinic.validation.group.OnClientCreate;
import com.bsdclinic.validation.group.OnCommonCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentDto {
    @NotBlank(message = "{validation.required.email}", groups = OnCommonCreate.class)
    @Size(max = 255, message = "{validation.input.max_length.255}", groups = OnCommonCreate.class)
    @Pattern(regexp = ValidationConstant.EMAIL_PATTERN, message = "{validation.format.email}", groups = OnCommonCreate.class)
    private String subscriberEmail;

    @NotBlank(message = "{validation.required.full_name}", groups = OnCommonCreate.class)
    @Size(max = 255, message = "{validation.input.max_length.255}", groups = OnCommonCreate.class)
    private String subscriberFullName;

    @NotBlank(message = "{validation.required.phone}", groups = OnCommonCreate.class)
    @Size(max = 20, message = "{validation.input.max_length.20}", groups = OnCommonCreate.class)
    private String subscriberPhone;

    @AppointmentRuleAnnotation.ValidRegisterDate(groups = OnClientCreate.class)
    private String registerDate;

    @AppointmentRuleAnnotation.ValidRegisterHour(groups = OnClientCreate.class)
    private String registerTime;

    @Size(max = 255, message = "{validation.input.max_length.255}", groups = OnCommonCreate.class)
    @Pattern(regexp = ValidationConstant.EMAIL_PATTERN, message = "{validation.format.email}", groups = OnCommonCreate.class)
    private String patientEmail;

    @NotBlank(message = "{validation.required.full_name}", groups = OnCommonCreate.class)
    @Size(max = 255, message = "{validation.input.max_length.255}", groups = OnCommonCreate.class)
    private String patientFullName;

    @Size(max = 20, message = "{validation.input.max_length.20}", groups = OnCommonCreate.class)
    private String patientPhone;

    @Size(max = 255, message = "{validation.input.max_length.255}", groups = OnCommonCreate.class)
    private String patientAddress;

    @NotBlank(message = "{validation.required.relation_with_subscriber}", groups = OnCommonCreate.class)
    @Size(max = 50, message = "{validation.input.max_length.50}", groups = OnCommonCreate.class)
    private String relationWithSubscriber;

    @NotBlank(message = "{validation.required.visit_reason}", groups = OnCommonCreate.class)
    @Size(max = 255, message = "{validation.input.max_length.255}", groups = OnCommonCreate.class)
    private String visitReason;

    @NotBlank(message = "{validation.required.gender}", groups = OnCommonCreate.class)
    @Pattern(regexp = ValidationConstant.GENDER_PATTERN, message = "{validation.invalid.gender}", groups = OnCommonCreate.class)
    private String patientGender;

    @AppointmentRuleAnnotation.ValidBirthday(groups = OnCommonCreate.class)
    private String patientBirthday;

    @NotBlank(message = "{validation.required.doctor_id}", groups = OnAdminCreate.class)
    @AppointmentRuleAnnotation.ValidDoctorId(groups = OnAdminCreate.class)
    private String doctorId;
}
