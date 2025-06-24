package com.bsdclinic;

import com.bsdclinic.dto.request.CreatePrescriptionRequest;
import com.bsdclinic.dto.response.CreatePrescriptionResponse;
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
    public CreatePrescriptionResponse createPrescription(@RequestBody @Valid CreatePrescriptionRequest createPrescriptionRequest) {
        return prescriptionService.createPrescription(createPrescriptionRequest);
    }
}
