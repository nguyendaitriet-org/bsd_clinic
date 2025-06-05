package com.bsdclinic.controller;

import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.UserService;
import com.bsdclinic.clinic_info.DayOfWeek;
import com.bsdclinic.dto.response.IUserSelectResponse;
import com.bsdclinic.url.WebUrl;
import com.bsdclinic.user.RoleConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AppointmentController {
    private final UserService userService;

    @ModelAttribute("doctors")
    public List<IUserSelectResponse> getDoctors() {
        return userService.getUsersForSelectByRoles(List.of(RoleConstant.DOCTOR.name()));
    }

    @RoleAuthorization.AuthenticatedUser
    @GetMapping(WebUrl.ADMIN_APPOINTMENT_INDEX)
    public String toAdminIndexPage() {
        return "admin/appointment/index";
    }

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
