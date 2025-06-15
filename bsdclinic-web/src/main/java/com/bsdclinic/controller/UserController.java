package com.bsdclinic.controller;

import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.UserService;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.url.WebUrl;
import com.bsdclinic.user.Role;
import com.bsdclinic.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final MessageProvider messageProvider;

    @ModelAttribute("userRoles")
    public List<Role> getAllUserRoles() {
        return userService.getAllRoles();
    }

    @ModelAttribute("userStatusMap")
    public Map<String, String> getUserStatus() {
        return messageProvider.getMessageMap("user.status", UserStatus.getAllNames());
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