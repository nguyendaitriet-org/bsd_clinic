package com.bsdclinic.controller;

import com.bsdclinic.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminSiteController {
    @GetMapping({"/login", "/admin"})
    public String toLoginPage(@AuthenticationPrincipal UserPrincipal principal) {
        return principal != null ? "admin/index" : "admin/auth/login";
    }
}
