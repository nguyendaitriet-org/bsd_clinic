package com.bsdclinic;

import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MedicalRecordApi {
    private final MedicalRecordService medicalRecordService;

    @RoleAuthorization.AdminAndDoctorAuthorization
    @PostMapping(WebUrl.API_ADMIN_MEDICAL_RECORD)
    public void createMedicalRecord(@RequestParam String appointmentId) {
        medicalRecordService.createMedicalRecord(appointmentId);
    }
}
