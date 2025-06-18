package com.bsdclinic.dto.request;

import com.bsdclinic.validation.AppointmentRuleAnnotation;
import com.bsdclinic.validation.ValidationConstant;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MedicalRecordUpdateRequest {
    @NotBlank(message = "{validation.required.full_name}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String patientName;

    @AppointmentRuleAnnotation.ValidBirthday
    private String patientBirthday;

    @NotBlank(message = "{validation.required.gender}")
    @Pattern(regexp = ValidationConstant.GENDER_PATTERN, message = "{validation.invalid.gender}")
    private String patientGender;

    @Size(max = 20, message = "{validation.input.max_length.20}")
    @Pattern(regexp = ValidationConstant.PHONE_NUMBER_PATTERN, message = "{validation.invalid.phone}")
    private String patientPhone;

    @Size(max = 255, message = "{validation.input.max_length.255}")
    @Pattern(regexp = ValidationConstant.EMAIL_PATTERN, message = "{validation.format.email}")
    private String patientEmail;

    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String patientAddress;

    @NotBlank(message = "{validation.required.visit_reason}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String visitReason;

    @Size(max = 1000, message = "{validation.input.max_length.1000}")
    private String medicalHistory;
    
    @NotBlank(message = "{validation.required.diagnosis}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String diagnosis;

    @DecimalMin(value = "0.0", message = "{validation.must_be_positive.advance}")
    private BigDecimal advance;

    private List<String> medicalServiceIds;
}
