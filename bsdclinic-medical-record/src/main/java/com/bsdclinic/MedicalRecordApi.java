package com.bsdclinic;

import com.bsdclinic.dto.request.MedicalRecordFilter;
import com.bsdclinic.dto.request.MedicalRecordUpdateRequest;
import com.bsdclinic.dto.response.MedicalRecordResponse;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MedicalRecordApi {
    private final MedicalRecordService medicalRecordService;

    @RoleAuthorization.AdminAndDoctorAuthorization
    @PostMapping(WebUrl.API_ADMIN_MEDICAL_RECORD_LIST)
    public DatatableResponse getMedicalRecords(@RequestBody MedicalRecordFilter medicalRecordFilter) {
        return medicalRecordService.getMedicalRecordsByFilter(medicalRecordFilter);
    }

    @RoleAuthorization.AdminAndDoctorAuthorization
    @PostMapping(WebUrl.API_ADMIN_MEDICAL_RECORD)
    public MedicalRecordResponse createMedicalRecord(@RequestBody MedicalRecordResponse medicalRecordResponse) {
        return medicalRecordService.createMedicalRecord(medicalRecordResponse.getAppointmentId());
    }

    @RoleAuthorization.AdminAndDoctorAuthorization
    @PutMapping(WebUrl.API_ADMIN_MEDICAL_RECORD_APPOINTMENT_UPDATE)
    public void updateMedicalRecordAndAppointment(
            @PathVariable String medicalRecordId,
            @PathVariable String appointmentId,
            @RequestBody @Valid MedicalRecordUpdateRequest request
    ) {
        medicalRecordService.updateMedicalRecordAndAppointment(medicalRecordId, appointmentId, request);
    }
}
