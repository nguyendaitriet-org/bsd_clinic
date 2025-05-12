package com.bsdclinic.controller;

import com.bsdclinic.RoleAuthorization;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/appointments")
public class AppointmentController {
    @RoleAuthorization.AuthenticatedUser
    @GetMapping("/create")
    public String toCreate() {
        return "admin/appointment/create";
    }
}
