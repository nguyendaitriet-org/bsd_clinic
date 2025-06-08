package com.bsdclinic;

import com.bsdclinic.admin.AdminAppointmentService;
import com.bsdclinic.client.ClientAppointmentService;
import com.bsdclinic.client.response.AvailableAppointmentSlot;
import com.bsdclinic.dto.AppointmentDto;
import com.bsdclinic.dto.request.AppointmentFilter;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.url.WebUrl;
import com.bsdclinic.validation.AppointmentRuleAnnotation;
import com.bsdclinic.validation.group.OnAdminCreate;
import com.bsdclinic.validation.group.OnClientCreate;
import com.bsdclinic.validation.group.OnCommonCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Validated
public class AppointmentApi {
    private final ClientAppointmentService clientAppointmentService;
    private final AdminAppointmentService adminAppointmentService;

    /*--------------------- CLIENT API ----------------------*/
    @GetMapping(WebUrl.API_CLIENT_APPOINTMENT_AVAILABLE_SLOTS)
    public AvailableAppointmentSlot getAvailableSlots(@RequestParam @AppointmentRuleAnnotation.ValidRegisterDate String registerDate) {
        return clientAppointmentService.getAvailableSlots(LocalDate.parse(registerDate));
    }

    @PostMapping(WebUrl.API_CLIENT_APPOINTMENT)
    public void createAppointmentByClient(
            @RequestBody @Validated({OnClientCreate.class, OnCommonCreate.class}) AppointmentDto request
    ) {
        clientAppointmentService.createNewAppointment(request);
    }
    /*------------------------------------------------------*/


    /*--------------------- ADMIN API ----------------------*/
    @RoleAuthorization.AuthenticatedUser
    @PostMapping(WebUrl.API_ADMIN_APPOINTMENT)
    public void createAppointmentByAdmin(
            @RequestBody @Validated({OnAdminCreate.class, OnCommonCreate.class}) AppointmentDto request
    ) {
        adminAppointmentService.createNewAppointment(request);
    }

    @RoleAuthorization.AuthenticatedUser
    @PostMapping(WebUrl.API_ADMIN_APPOINTMENT_LIST)
    public DatatableResponse getAppointmentsByFilter(@RequestBody AppointmentFilter appointmentFilter) {
        return adminAppointmentService.getAppointmentsByFilter(appointmentFilter);
    }
    /*------------------------------------------------------*/
}
