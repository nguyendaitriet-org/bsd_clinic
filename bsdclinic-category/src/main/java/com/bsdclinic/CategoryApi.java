package com.bsdclinic;

import com.bsdclinic.dto.request.CategoryRequest;
import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CategoryApi {
    private final CategoryService categoryService;

    @RoleAuthorization.AdminAndDoctorAuthorization
    @PostMapping(WebUrl.API_ADMIN_CATEGORY)
    public void createCategories(@RequestBody @Valid CategoryRequest categories) {
        categoryService.createCategories(categories);
    }
}
