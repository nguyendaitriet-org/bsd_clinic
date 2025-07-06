package com.bsdclinic;

import com.bsdclinic.clinic_info.ClinicInfo;
import com.bsdclinic.validation.ClinicInfoRuleAnnotation;
import com.bsdclinic.validation.ValidationConstant;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private Map<String, List<Map<String, List<String>>>> workingHoursJson;

    private List<LocalDate> dayOffs;

    public List<LocalDate> getDayOffs() {
        return dayOffs == null ? List.of() : dayOffs;
    }

    public Map<String, List<Map<String, List<String>>>> convertWorkingHours(
            Map<String, List<ClinicInfo.TimeRange>> input
    ) {
        return input.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(timeRange -> {
                                    Map<String, List<String>> timeMap = new HashMap<>();
                                    timeMap.put("start", List.of(timeRange.start().toString()));
                                    timeMap.put("end", List.of(timeRange.end().toString()));
                                    return timeMap;
                                })
                                .collect(Collectors.toList())
                ));
    }
}
