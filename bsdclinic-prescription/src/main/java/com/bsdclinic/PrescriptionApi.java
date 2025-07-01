package com.bsdclinic;

import com.bsdclinic.dto.request.CreatePrescriptionRequest;
import com.bsdclinic.dto.response.PrescriptionResponse;
import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PrescriptionApi {
    private final PrescriptionService prescriptionService;

    @RoleAuthorization.AdminAndDoctorAuthorization
    @PostMapping(WebUrl.API_ADMIN_PRESCRIPTION)
    public PrescriptionResponse createPrescription(@RequestBody @Valid CreatePrescriptionRequest createPrescriptionRequest) {
        return prescriptionService.createPrescription(createPrescriptionRequest);
    }

    @GetMapping(WebUrl.API_ADMIN_PRESCRIPTION_WITH_ID)
    public PrescriptionResponse getPrescription(@PathVariable String prescriptionId) {
        return prescriptionService.getPrescription(prescriptionId);
    }

    @RoleAuthorization.AdminAndDoctorAuthorization
    @DeleteMapping(WebUrl.API_ADMIN_PRESCRIPTION_WITH_ID)
    public void deletePrescription(@PathVariable String prescriptionId) {
        prescriptionService.deletePrescription(prescriptionId);
    }
}
