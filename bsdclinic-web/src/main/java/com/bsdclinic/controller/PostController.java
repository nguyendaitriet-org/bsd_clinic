package com.bsdclinic.controller;

import com.bsdclinic.ClinicInfoDto;
import com.bsdclinic.ClinicInfoService;
import com.bsdclinic.url.WebUrl;
import com.bsdclinic.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final ClinicInfoService clinicInfoService;

    @ModelAttribute("clinicInfo")
    public ClinicInfoDto getClinicInfo() {
        return clinicInfoService.getClinicInfo();
    }

    @GetMapping(WebUrl.ACNE_TREATMENT_BLOG)
    public String toPost1Page() {
        return "client/post/acne_treatment_blog";
    }

    @GetMapping(WebUrl.SKIN_REJUVENATION)
    public String toPostSkinRejuvenation() {
        return "client/post/skin_rejuvenation";
    }

    @GetMapping(WebUrl.LASER_BEAUTY_BLOG)
    public String toPost3Page() {
        return "client/post/laser_beauty_blog";
    }

    @GetMapping(WebUrl.BOX_FILLER_BLOG)
    public String toPostBoxAndFillerPage() {
        return "client/post/box_filler_blog";
    }

    @GetMapping(WebUrl.BIO_LIGHT_BLOG)
    public String toPostBioLightPage() {
        return "client/post/bio_light_blog";
    }

    @GetMapping(WebUrl.SKINCARE_BLOG)
    public String toSkinCareBlogPage() {
        return "client/post/skincare_blog";
    }

    @GetMapping(WebUrl.ACNE_DURING_PREGNANCY_BLOG)
    public String toPost7Page() {
        return "client/post/acne_during_pregnancy_blog";
    }

    @GetMapping(WebUrl.CLINIC_INTRODUCTION_PAGE)
    public String toPost8Page() {
        return "client/post/clinic_introduction_page";
    }
}

