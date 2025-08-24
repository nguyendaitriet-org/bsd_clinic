package com.bsdclinic.controller;

import com.bsdclinic.CategoryService;
import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.category.CategoryType;
import com.bsdclinic.dto.request.CategoryListRequest;
import com.bsdclinic.dto.response.CategoryResponse;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MedicalServiceController {
    private final CategoryService categoryService;

    @ModelAttribute("medicalServiceCategories")
    public List<CategoryResponse> getMedicineCategories() {
        CategoryListRequest request = CategoryListRequest.builder()
                .categoryType(CategoryType.MEDICAL_SERVICE.name())
                .build();
        return categoryService.getCategories(request);
    }

    @RoleAuthorization.AdminAuthorization
    @GetMapping(WebUrl.ADMIN_MEDICAL_SERVICE_INDEX)
    public String toIndex() {
        return "admin/service/index";
    }
}
