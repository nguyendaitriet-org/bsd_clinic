package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicalServiceRequest;
import com.bsdclinic.dto.request.MedicalServiceFilter;
import com.bsdclinic.dto.request.MedicalServiceUpdateRequest;
import com.bsdclinic.dto.response.MedicalServiceResponse;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.medical_service.MedicalService;
import com.bsdclinic.medicine.Medicine;
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
    @PutMapping(WebUrl.API_ADMIN_MEDICAL_SERVICE_WITH_ID)
    public void updateMedicalService(
            @PathVariable String medicalServiceId,
            @RequestBody @Valid MedicalServiceUpdateRequest request
    ) {
        serviceMedicalService.updateMedicalService(medicalServiceId, request);
    }

    @RoleAuthorization.AdminAuthorization
    @DeleteMapping(WebUrl.API_ADMIN_MEDICAL_SERVICE_WITH_ID)
    public void updateMedicine(@PathVariable String medicalServiceId) {
        serviceMedicalService.deleteMedicalService(medicalServiceId);
    }
}
