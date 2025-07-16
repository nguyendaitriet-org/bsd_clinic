package com.bsdclinic.controller;

import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.UserService;
import com.bsdclinic.admin.AdminAppointmentService;
import com.bsdclinic.dto.response.IUserSelectResponse;
import com.bsdclinic.url.WebUrl;
import com.bsdclinic.user.RoleConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AppointmentController {
    private final UserService userService;
    private final AdminAppointmentService adminAppointmentService;

    @ModelAttribute("doctors")
    public List<IUserSelectResponse> getDoctors() {
        return userService.getUsersForSelectByRoles(List.of(RoleConstant.DOCTOR.name()));
    }

    @ModelAttribute("doctorMap")
    public Map<String, String> getDoctorMap() {
        adminAppointmentService.getAppointmentStatusCount();
        return userService.getUsersForSelectByRoles(List.of(RoleConstant.DOCTOR.name())).stream()
                .collect(Collectors.toMap(
                        IUserSelectResponse::getUserId,
                        IUserSelectResponse::getFullName
                ));
    }

    @ModelAttribute("appointmentStatusCount")
    public Map<String, Integer> getAppointmentStatusCount() {
        return adminAppointmentService.getAppointmentStatusCount();
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

    @RoleAuthorization.AdminAndDoctorAuthorization
    @GetMapping(WebUrl.ADMIN_APPOINTMENT_FOR_DOCTOR)
    public String toAppointmentForDoctorPage() {
        return "admin/appointment/for_doctor";
    }
}
