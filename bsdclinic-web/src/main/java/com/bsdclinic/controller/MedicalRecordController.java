package com.bsdclinic.controller;

import com.bsdclinic.MedicalRecordService;
import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.admin.AdminAppointmentService;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MedicalRecordController {
    private final AdminAppointmentService adminAppointmentService;
    private final MedicalRecordService medicalRecordService;

    @RoleAuthorization.AdminAndDoctorAuthorization
    @GetMapping(WebUrl.ADMIN_MEDICAL_RECORD_DETAIL)
    public String toMedicalRecordDetail(@PathVariable String medicalRecordId, @PathVariable String appointmentId) {
        if (!adminAppointmentService.existsAppointment(appointmentId) ||
                !medicalRecordService.existsMedicalRecord(medicalRecordId)) {
            return "error/error404";
        }

        return "admin/medical_record/detail";
    }
}
