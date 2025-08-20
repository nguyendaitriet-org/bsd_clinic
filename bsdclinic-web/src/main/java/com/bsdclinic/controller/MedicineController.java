package com.bsdclinic.controller;

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
public class MedicineController {
    private final MessageProvider messageProvider;
    private final CategoryService categoryService;

    @ModelAttribute("dosageUnits")
    public Map<String, String> getDosageUnits() {
        return messageProvider.getMessageMap("medicine.dosage_unit", "constants");
    }

    @ModelAttribute("medicineCategories")
    public List<CategoryResponse> getMedicineCategories() {
        CategoryListRequest request = CategoryListRequest.builder()
                .categoryType(CategoryType.MEDICINE.name())
                .build();
        return categoryService.getCategories(request);
    }

    @RoleAuthorization.AdminAndDoctorAuthorization
    @GetMapping(WebUrl.ADMIN_MEDICINE_INDEX)
    public String toIndex() {
        return "admin/medicine/index";
    }
}
