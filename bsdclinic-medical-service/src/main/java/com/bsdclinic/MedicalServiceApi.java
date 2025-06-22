package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicalServiceRequest;
import com.bsdclinic.dto.request.MedicalServiceFilter;
import com.bsdclinic.dto.response.MedicalServiceResponse;
import com.bsdclinic.medical_service.MedicalService;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MedicalServiceApi {
    private final ServiceMedicalService serviceMedicalService;

    @RoleAuthorization.AdminAuthorization
    @PostMapping(WebUrl.API_ADMIN_MEDICAL_SERVICE)
    public void createMedicalService(@RequestBody @Valid CreateMedicalServiceRequest request) {
        serviceMedicalService.create(request);
    }

    @RoleAuthorization.AdminAuthorization
    @PostMapping(WebUrl.API_ADMIN_MEDICAL_SERVICE_LIST)
    public DatatableResponse getMedicalServices(@RequestBody MedicalServiceFilter request) {
        return serviceMedicalService.getMedicalServices(request);
    }

    @RoleAuthorization.AdminAuthorization
    @GetMapping(WebUrl.API_ADMIN_MEDICAL_SERVICE)
    public List<MedicalServiceResponse> getMedicalServicesForSelection(@RequestParam String keyword) {
        return serviceMedicalService.getMedicalServicesForSelection(keyword);
    }
    @RoleAuthorization.AdminAuthorization
    @PatchMapping(WebUrl.API_ADMIN_MEDICAL_SERVICE)
    public void updateMedicalService(@AuthenticationPrincipal MedicalService medicalService, @RequestBody @Valid CreateMedicalServiceRequest request) throws IOException {
        serviceMedicalService.updateMedicalService(medicalService.getMedicalServiceId(), request);
    }
}
