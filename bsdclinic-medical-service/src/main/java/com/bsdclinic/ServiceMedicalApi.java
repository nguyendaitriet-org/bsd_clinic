package com.bsdclinic;

import com.bsdclinic.dto.request.CreateServiceMedicalRequest;
import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ServiceMedicalApi {
    private final ServiceMedicalService serviceMedicalService;

    @RoleAuthorization.AdminAuthorization
    @PostMapping(WebUrl.API_ADMIN_MEDICAL_SERVICE)
    public void createServiceMedical(@RequestBody @Valid CreateServiceMedicalRequest request) {
        serviceMedicalService.create(request);
    }
}
