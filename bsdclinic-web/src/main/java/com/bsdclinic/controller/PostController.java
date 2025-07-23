package com.bsdclinic.controller;

import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PostController {

    @GetMapping(WebUrl.CLIENT_ACNE_TREATMENT_BLOG)
    public String toPost1Page() {
        return "client/post/acne_treatment_blog";
    }

    @GetMapping(WebUrl.CLIENT_SKIN_REJUVENATION)
    public String toPostSkinRejuvenation() {
        return "client/post/skin_rejuvenation";
    }

    @GetMapping(WebUrl.CLIENT_POST_LASER_BEAUTY_BLOG)
    public String toPost3Page() {
        return "client/post/laser_beauty_blog";
    }
    @GetMapping(WebUrl.CLIENT_POST_BOX_FILLER_BLOG)
    public String toPostBoxAndFillerPage() {
        return "client/post/box_filler_blog";
    }
    @GetMapping(WebUrl.CLIENT_POST_5)
    public String toPost5Page() {
        return "client/post/post5";
    }
    @GetMapping(WebUrl.SKINCARE_BLOG)
    public String toSkinCareBlogPage() {
        return "client/post/skincare_blog";
    }
    @GetMapping(WebUrl.CLIENT_POST_7)
    public String toPost7Page() {
        return "client/post/post7";
    }
    @GetMapping(WebUrl.CLIENT_POST_8)
    public String toPost8Page() {
        return "post7";
    }
}

