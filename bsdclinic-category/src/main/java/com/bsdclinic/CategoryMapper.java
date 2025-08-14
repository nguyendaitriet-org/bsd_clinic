package com.bsdclinic;

import com.bsdclinic.category.Category;
import com.bsdclinic.dto.request.CategoryRequest;
import com.bsdclinic.dto.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toEntity(CategoryRequest categoryRequest);
    List<CategoryResponse> toDtos(List<Category> categories);
}
