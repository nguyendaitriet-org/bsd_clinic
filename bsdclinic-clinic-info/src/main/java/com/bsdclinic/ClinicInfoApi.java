package com.bsdclinic;

import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClinicInfoApi {
    private final ClinicInfoService clinicInfoService;

    @GetMapping(WebUrl.API_CLIENT_CLINIC_INFO)
    public ClinicInfoDto getClinicInfo() {
        return clinicInfoService.getClinicInfo();
    }

    @PutMapping(WebUrl.API_CLIENT_UPDATE_CLINIC_INFO)
    @RoleAuthorization.AdminAuthorization
    public void updateClinicInfo(
            @PathVariable String clinicInfoId,
            @RequestBody @Valid ClinicInfoDto request
    ) {
        clinicInfoService.updateClinicInfo(clinicInfoId, request);
    }
}
