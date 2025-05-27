package com.bsdclinic.controller;

import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.url.WebUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppointmentController {
    @RoleAuthorization.AuthenticatedUser
    @GetMapping(WebUrl.ADMIN_APPOINTMENT_CREATE)
    public String toAdminCreatePage() {
        return "admin/appointment/create";
    }

    @GetMapping(WebUrl.CLIENT_APPOINTMENT_CREATE)
    public String toClientCreatePage() {
        return "client/appointment/create";
    }
}
