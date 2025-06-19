package com.bsdclinic.controller;

import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MedicalServiceController {
    @RoleAuthorization.AdminAuthorization
    @GetMapping(WebUrl.ADMIN_SERVICE_INDEX)
    public String toIndex() {
        return "admin/service/index";
    }
}
