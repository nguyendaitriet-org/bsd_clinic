package com.bsdclinic.controller;

import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.UserService;
import com.bsdclinic.url.WebUrl;
import com.bsdclinic.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ModelAttribute("userRoles")
    public List<Role> getAllUserRoles() {
        return userService.getAllRoles();
    }

    @RoleAuthorization.AdminAuthorization
    @GetMapping(WebUrl.ADMIN_USERS_INDEX)
    public String toIndex() {
        return "admin/user/index";
    }

    @RoleAuthorization.AuthenticatedUser
    @GetMapping(WebUrl.ADMIN_USERS_PROFILE)
    public String toProfile() {
        return "admin/user/profile";
    }
}
