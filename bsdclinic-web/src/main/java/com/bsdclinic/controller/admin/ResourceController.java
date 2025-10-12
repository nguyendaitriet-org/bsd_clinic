package com.bsdclinic.controller.admin;

import com.bsdclinic.CategoryService;
import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.category.CategoryType;
import com.bsdclinic.dto.request.CategoryListRequest;
import com.bsdclinic.dto.response.CategoryResponse;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ResourceController {
    private final MessageProvider messageProvider;
    private final CategoryService categoryService;

    @ModelAttribute("resourceTypes")
    public Map<String, String> getCategoryTypes() {
        return messageProvider.getMessageMap("resource.type", "constants");
    }

    @ModelAttribute("entityCategories")
    public List<CategoryResponse> getMedicineCategories() {
        CategoryListRequest request = CategoryListRequest.builder()
                .categoryType(CategoryType.RESOURCE.name())
                .build();
        return categoryService.getCategories(request);
    }

    @RoleAuthorization.AdminAuthorization
    @GetMapping(WebUrl.ADMIN_RESOURCE_INDEX)
    public String toIndex() {
        return "admin/storage/index";
    }
}
