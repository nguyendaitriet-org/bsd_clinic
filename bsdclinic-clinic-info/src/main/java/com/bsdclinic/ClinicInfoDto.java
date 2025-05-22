package com.bsdclinic;

import com.bsdclinic.clinic_info.ClinicInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ClinicInfoDto {
    private String name;

    private String address;

    private String phone;

    private String email;

    private Map<String, List<ClinicInfo.TimeRange>> workingHours;

    private String website;

    private String introduction;

    private String slogan;

    private String description;

    private Boolean isActive;
}
