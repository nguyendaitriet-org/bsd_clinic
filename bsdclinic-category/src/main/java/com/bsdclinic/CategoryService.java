package com.bsdclinic;

import com.bsdclinic.dto.request.CategoryListRequest;
import com.bsdclinic.dto.request.CategoryCreateRequest;
import com.bsdclinic.dto.request.CategoryUpdateRequest;
import com.bsdclinic.dto.response.CategoryResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CategoryService {
    void createCategory(CategoryCreateRequest categoryCreateRequest);
    List<CategoryResponse> getCategories(CategoryListRequest request);
    void updateCategory(String categoryId, CategoryUpdateRequest categoryUpdateRequest);
    void deleteCategory(String categoryId);
    void createCategoryAssignments(String entityId, String entityTitle, Set<String> categoryIds);
    Map<String, List<CategoryResponse>> getAssignmentsByEntityIds(List<String> entityIds);
    void deleteAssignmentByEntityId(String entityId);
}
