package com.bsdclinic.controller.admin;

import com.bsdclinic.UserPrincipal;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomePageController {
    @GetMapping(WebUrl.ADMIN_HOME)
    public String toAdminHomePage() {
        return "admin/index";
    }

    @GetMapping(WebUrl.LOGIN)
    public String toAdminLoginPage(@AuthenticationPrincipal UserPrincipal principal) {
        return principal != null ? "redirect:/admin" : "admin/auth/login";
    }
}
