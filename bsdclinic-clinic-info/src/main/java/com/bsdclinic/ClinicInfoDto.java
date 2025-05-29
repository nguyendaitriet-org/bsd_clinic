package com.bsdclinic;

import com.bsdclinic.clinic_info.ClinicInfo;
import com.bsdclinic.validation.ClinicInfoRuleAnnotation;
import com.bsdclinic.validation.ValidationConstant;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ClinicInfoDto {
    private String clinicInfoId;

    @NotBlank(message = "{validation.required.clinic_name}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String name;

    @NotBlank(message = "{validation.required.clinic_address}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String address;

    @NotBlank(message = "{validation.required.clinic_phone}")
    @Size(max = 20, message = "{validation.input.max_length.20}")
    private String phone;

    @Size(max = 255, message = "{validation.input.max_length.255}")
    @Pattern(regexp = ValidationConstant.EMAIL_PATTERN, message = "{validation.format.email}")
    private String email;

    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String website;

    @Size(max = 5000, message = "{validation.input.max_length.5000}")
    private String introduction;

    @Size(max = 5000, message = "{validation.input.max_length.5000}")
    private String slogan;

    @Size(max = 5000, message = "{validation.input.max_length.5000}")
    private String description;

    @NotNull(message = "{validation.required.clinic_active}")
    private Boolean isActive;

    @ClinicInfoRuleAnnotation.ValidRegisterTimeRange
    private Integer registerTimeRange;

    @NotEmpty(message = "{validation.required.working_hours}")
    private Map<String, List<ClinicInfo.TimeRange>> workingHours;

    private List<LocalDate> dayOffs;
}
