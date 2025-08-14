package com.bsdclinic;

import com.bsdclinic.dto.request.CategoryListRequest;
import com.bsdclinic.dto.request.CategoryRequest;
import com.bsdclinic.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    void createCategories(CategoryRequest categoryRequest);
    List<CategoryResponse> getCategories(CategoryListRequest request);
}
