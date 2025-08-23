package com.bsdclinic;

import com.bsdclinic.dto.request.MedicalServiceRequest;
import com.bsdclinic.dto.request.MedicalServiceFilter;
import com.bsdclinic.dto.response.MedicalServiceResponse;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MedicalServiceApi {
    private final ServiceMedicalService serviceMedicalService;

    @RoleAuthorization.AdminAuthorization
    @PostMapping(WebUrl.API_ADMIN_MEDICAL_SERVICE)
    public void createMedicalService(@RequestBody @Valid MedicalServiceRequest request) {
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
    @PutMapping(WebUrl.API_ADMIN_MEDICAL_SERVICE_WITH_ID)
    public void updateMedicalService(
            @PathVariable String medicalServiceId,
            @RequestBody @Valid MedicalServiceRequest request
    ) {
        serviceMedicalService.updateMedicalService(medicalServiceId, request);
    }

    @RoleAuthorization.AdminAuthorization
    @DeleteMapping(WebUrl.API_ADMIN_MEDICAL_SERVICE_WITH_ID)
    public void updateMedicalService(@PathVariable String medicalServiceId) {
        serviceMedicalService.deleteMedicalService(medicalServiceId);
    }
}
