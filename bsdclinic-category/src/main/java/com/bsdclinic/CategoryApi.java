package com.bsdclinic;

import com.bsdclinic.dto.request.CategoryCreateRequest;
import com.bsdclinic.dto.request.CategoryListRequest;
import com.bsdclinic.dto.request.CategoryUpdateRequest;
import com.bsdclinic.dto.response.CategoryResponse;
import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryApi {
    private final CategoryService categoryService;

    @RoleAuthorization.AdminAndDoctorAuthorization
    @PostMapping(WebUrl.API_ADMIN_CATEGORY)
    public void createCategories(@RequestBody @Valid CategoryCreateRequest request) {
        categoryService.createCategory(request);
    }

    @RoleAuthorization.AdminAndDoctorAuthorization
    @GetMapping(WebUrl.API_ADMIN_CATEGORY)
    public List<CategoryResponse> getCategories(@ModelAttribute @Valid CategoryListRequest request) {
        return categoryService.getCategories(request);
    }

    @RoleAuthorization.AdminAndDoctorAuthorization
    @PatchMapping(WebUrl.API_ADMIN_CATEGORY_WITH_ID)
    public void updateCategories(
            @PathVariable String categoryId,
            @RequestBody @Valid CategoryUpdateRequest request
    ) {
        categoryService.updateCategory(categoryId, request);
    }
}
