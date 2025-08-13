package com.bsdclinic.controller.client;

import com.bsdclinic.ClinicInfoDto;
import com.bsdclinic.ClinicInfoService;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller(value = "clientPostController")
@RequiredArgsConstructor
public class PostController {
    private final ClinicInfoService clinicInfoService;

    @ModelAttribute("clinicInfo")
    public ClinicInfoDto getClinicInfo() {
        return clinicInfoService.getClinicInfo();
    }

    @GetMapping(WebUrl.ACNE_TREATMENT_BLOG)
    public String toAcneTreatmentBlog() {
        return "client/post/acne_treatment_blog";
    }

    @GetMapping(WebUrl.SKIN_REJUVENATION_BLOG)
    public String toPostSkinRejuvenationBlog() {
        return "client/post/skin_rejuvenation";
    }

    @GetMapping(WebUrl.LASER_BEAUTY_BLOG)
    public String toLaserBeautyBlog() {
        return "client/post/laser_beauty_blog";
    }

    @GetMapping(WebUrl.BOTOX_FILLER_BLOG)
    public String toPostBoxAndFillerBlog() {
        return "client/post/botox_filler_blog";
    }

    @GetMapping(WebUrl.BIO_LIGHT_BLOG)
    public String toPostBioLightBlog() {
        return "client/post/bio_light_blog";
    }

    @GetMapping(WebUrl.SKINCARE_BLOG)
    public String toSkinCareBlogBlog() {
        return "client/post/skincare_blog";
    }

    @GetMapping(WebUrl.ACNE_DURING_PREGNANCY_BLOG)
    public String toAcneDuringPregnancyBlog() {
        return "client/post/acne_during_pregnancy_blog";
    }

    @GetMapping(WebUrl.CLINIC_INTRODUCTION_PAGE)
    public String toClinicIntroductionPage() {
        return "client/post/clinic_introduction_page";
    }
}

