package com.bsdclinic.controller;

import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.url.WebUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MedicalRecordController {
    @RoleAuthorization.AdminAndDoctorAuthorization
    @GetMapping(WebUrl.ADMIN_MEDICAL_RECORD_DETAIL)
    public String toMedicalRecordDetail(@PathVariable String medicalRecordId) {
        return "admin/medical_record/detail";
    }
}
