package com.bsdclinic.controller;

import com.bsdclinic.url.WebUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClinicInfoController {
    @GetMapping(WebUrl.ADMIN_CLINIC_INFO)
    public String toIndex() {
        return "admin/clinic/index";
    }
}
