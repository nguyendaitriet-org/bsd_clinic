package com.bsdclinic.controller.admin;

import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final MessageProvider messageProvider;

    @ModelAttribute("categoryTypes")
    public Map<String, String> getCategoryTypes() {
        return messageProvider.getMessageMap("category.type", "constants");
    }

    @RoleAuthorization.AdminAuthorization
    @GetMapping(WebUrl.ADMIN_CATEGORY_INDEX)
    public String toIndex() {
        return "admin/category/index";
    }
}
