package com.bsdclinic;

import com.bsdclinic.client.ClientAppointmentService;
import com.bsdclinic.client.response.AvailableAppointmentSlot;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping({WebUrl.API_ADMIN_APPOINTMENT, WebUrl.API_CLIENT_APPOINTMENT})
@RequiredArgsConstructor
public class AppointmentApi {
    private final ClientAppointmentService clientAppointmentService;

    @GetMapping("/available-slots")
    public AvailableAppointmentSlot getAvailableSlots(@RequestParam LocalDate registerDate) {
        return clientAppointmentService.getAvailableSlots(registerDate);
    }
}
