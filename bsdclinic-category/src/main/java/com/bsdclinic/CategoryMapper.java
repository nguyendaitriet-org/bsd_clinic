package com.bsdclinic;

import com.bsdclinic.category.Category;
import com.bsdclinic.category.CategoryAssignment;
import com.bsdclinic.dto.request.CategoryAssignmentRequest;
import com.bsdclinic.dto.request.CategoryCreateRequest;
import com.bsdclinic.dto.response.CategoryResponse;
import com.bsdclinic.dto.response.ICategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toEntity(CategoryCreateRequest categoryRequest);
    List<CategoryResponse> toDtos(List<Category> categories);
    List<CategoryAssignment> toEntities(List<CategoryAssignmentRequest> requestList);
    CategoryResponse toCategoryResponse(ICategoryResponse category);
}
