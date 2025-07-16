package com.bsdclinic.controller.client;

import com.bsdclinic.ClinicInfoDto;
import com.bsdclinic.ClinicInfoService;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller(value = "clientAppointmentController")
@RequiredArgsConstructor
public class AppointmentController {
    private final ClinicInfoService clinicInfoService;

    @ModelAttribute("clinicInfo")
    public ClinicInfoDto getClinicInfo() {
        return clinicInfoService.getClinicInfo();
    }

    @GetMapping(WebUrl.CLIENT_APPOINTMENT_CREATE)
    public String toClientCreatePage() {
        return "client/appointment/create";
    }

    @GetMapping(WebUrl.CLIENT_APPOINTMENT_CREATE_SUCCESS)
    public String toClientCreateSuccessfullyPage() {
        return "client/appointment/create_success";
    }
}
