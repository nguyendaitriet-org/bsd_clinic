package com.bsdclinic.controller;

import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.UserService;
import com.bsdclinic.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ModelAttribute("userRoles")
    public List<Role> getAllUserRoles() {
        return userService.getAllRoles();
    }

    @RoleAuthorization.AdminAuthorization
    @GetMapping
    public String toIndex() {
        return "admin/user/index";
    }
}
