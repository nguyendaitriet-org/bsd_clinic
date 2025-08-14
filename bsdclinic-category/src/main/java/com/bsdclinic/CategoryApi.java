package com.bsdclinic;

import com.bsdclinic.dto.request.CategoryListRequest;
import com.bsdclinic.dto.request.CategoryRequest;
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
    public void createCategories(@RequestBody @Valid CategoryRequest categories) {
        categoryService.createCategories(categories);
    }

    @RoleAuthorization.AdminAndDoctorAuthorization
    @GetMapping(WebUrl.API_ADMIN_CATEGORY)
    public List<CategoryResponse> getCategories(@ModelAttribute @Valid CategoryListRequest request) {
        return categoryService.getCategories(request);
    }
}
