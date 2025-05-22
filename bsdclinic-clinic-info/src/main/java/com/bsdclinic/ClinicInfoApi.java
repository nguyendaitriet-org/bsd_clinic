package com.bsdclinic;

import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WebUrl.API_CLIENT_CLINIC_INFO)
@RequiredArgsConstructor
public class ClinicInfoApi {
    private final ClinicInfoService clinicInfoService;

    @GetMapping
    public ClinicInfoDto getClinicInfo() {
        return clinicInfoService.getClinicInfo();
    }
}
