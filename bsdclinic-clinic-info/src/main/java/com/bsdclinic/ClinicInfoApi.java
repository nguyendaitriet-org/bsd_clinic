package com.bsdclinic;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/clinic-info")
@RequiredArgsConstructor
public class ClinicInfoApi {
    private final ClinicInfoService clinicInfoService;

    @GetMapping
    public ClinicInfoDto getClinicInfo() {
        return clinicInfoService.getClinicInfo();
    }
}
