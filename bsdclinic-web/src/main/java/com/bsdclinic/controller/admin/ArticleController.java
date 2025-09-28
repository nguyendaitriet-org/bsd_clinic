package com.bsdclinic.controller.admin;

import com.bsdclinic.CategoryService;
import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.UserService;
import com.bsdclinic.admin.AdminAppointmentService;
import com.bsdclinic.category.CategoryType;
import com.bsdclinic.dto.request.CategoryListRequest;
import com.bsdclinic.dto.response.CategoryResponse;
import com.bsdclinic.dto.response.IUserSelectResponse;
import com.bsdclinic.url.WebUrl;
import com.bsdclinic.user.RoleConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final CategoryService categoryService;

    @ModelAttribute("entityCategories")
    public List<CategoryResponse> getMedicineCategories() {
        CategoryListRequest request = CategoryListRequest.builder()
                .categoryType(CategoryType.ARTICLE.name())
                .build();
        return categoryService.getCategories(request);
    }

    @RoleAuthorization.AdminAndDoctorAuthorization
    @GetMapping(WebUrl.ADMIN_ARTICLE_CREATE)
    public String toCreatePage() {
        return "admin/article/create";
    }
}
