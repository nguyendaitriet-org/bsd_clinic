package com.bsdclinic.controller.client;

import com.bsdclinic.ClinicInfoDto;
import com.bsdclinic.ClinicInfoService;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller(value = "clientHomePageController")
@RequiredArgsConstructor
public class HomePageController {
    private final ClinicInfoService clinicInfoService;

    @ModelAttribute("clinicInfo")
    public ClinicInfoDto getClinicInfo() {
        return clinicInfoService.getClinicInfo();
    }

    @GetMapping(WebUrl.CLIENT_ZALO)
    public String toZaloPage() {
        return "client/zalo";
    }

}
