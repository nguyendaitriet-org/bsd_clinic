package com.bsdclinic;

import com.bsdclinic.category.CategoryAssignment;
import com.bsdclinic.dto.request.CategoryAssignmentRequest;
import com.bsdclinic.dto.request.CategoryListRequest;
import com.bsdclinic.dto.request.CategoryCreateRequest;
import com.bsdclinic.dto.request.CategoryUpdateRequest;
import com.bsdclinic.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    void createCategory(CategoryCreateRequest categoryCreateRequest);
    List<CategoryResponse> getCategories(CategoryListRequest request);
    void updateCategory(String categoryId, CategoryUpdateRequest categoryUpdateRequest);
    void deleteCategory(String categoryId);
    void createCategoryAssignments(List<CategoryAssignmentRequest> requestList);
}
