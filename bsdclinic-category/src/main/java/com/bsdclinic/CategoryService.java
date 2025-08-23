package com.bsdclinic;

import com.bsdclinic.dto.request.CategoryAssignmentRequest;
import com.bsdclinic.dto.request.CategoryListRequest;
import com.bsdclinic.dto.request.CategoryCreateRequest;
import com.bsdclinic.dto.request.CategoryUpdateRequest;
import com.bsdclinic.dto.response.CategoryResponse;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    void createCategory(CategoryCreateRequest categoryCreateRequest);
    List<CategoryResponse> getCategories(CategoryListRequest request);
    void updateCategory(String categoryId, CategoryUpdateRequest categoryUpdateRequest);
    void deleteCategory(String categoryId);
    void createCategoryAssignments(List<CategoryAssignmentRequest> requestList);
    Map<String, List<CategoryResponse>> getAssignmentsByEntityIds(List<String> entityIds);
    void deleteAssignmentByEntityId(String entityId);
}
