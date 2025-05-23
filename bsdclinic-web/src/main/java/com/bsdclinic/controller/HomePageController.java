package com.bsdclinic.controller;

import com.bsdclinic.UserPrincipal;
import com.bsdclinic.url.WebUrl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomePageController {
    @GetMapping(WebUrl.CLIENT_HOME)
    public String toClientHomePage() {
        return "client/index";
    }

    @GetMapping(WebUrl.LOGIN)
    public String toAdminLoginPage(@AuthenticationPrincipal UserPrincipal principal) {
        return principal != null ? "redirect:/admin" : "admin/auth/login";
    }

    @GetMapping(WebUrl.ADMIN_HOME)
    public ModelAndView toAdminHomePage() {
        return new ModelAndView("admin/index");
    }
}
