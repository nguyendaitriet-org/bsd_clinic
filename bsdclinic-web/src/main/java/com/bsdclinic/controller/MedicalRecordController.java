package com.bsdclinic.controller;

import com.bsdclinic.ServiceMedicalService;
import com.bsdclinic.dto.response.IMedicalServiceResponse;
import com.bsdclinic.dto.response.MedicalRecordResponse;
import com.bsdclinic.MedicalRecordService;
import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.admin.AdminAppointmentService;
import com.bsdclinic.dto.response.AppointmentResponse;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MedicalRecordController {
    private final AdminAppointmentService adminAppointmentService;
    private final MedicalRecordService medicalRecordService;
    private final ServiceMedicalService serviceMedicalService;

    @RoleAuthorization.AdminAndDoctorAuthorization
    @GetMapping(WebUrl.ADMIN_MEDICAL_RECORD_DETAIL)
    public ModelAndView toMedicalRecordDetail(@PathVariable String medicalRecordId, @PathVariable String appointmentId) {
        AppointmentResponse appointment;
        MedicalRecordResponse medicalRecord;
        List<IMedicalServiceResponse> medicalServices;
        try {
            appointment = adminAppointmentService.getAppointment(appointmentId);
            medicalRecord = medicalRecordService.getMedicalRecord(medicalRecordId);
            medicalServices = serviceMedicalService.getMedicalServicesByMedicalRecordId(medicalRecordId);
        } catch (NotFoundException e) {
            return new ModelAndView("error/error404");
        }

        ModelAndView mav = new ModelAndView("admin/medical_record/detail");
        mav.addObject("appointment", appointment);
        mav.addObject("medicalRecord", medicalRecord);
        mav.addObject("medicalServices", medicalServices);

        return mav;
    }
}
