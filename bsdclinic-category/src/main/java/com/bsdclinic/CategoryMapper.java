package com.bsdclinic;

import com.bsdclinic.category.Category;
import com.bsdclinic.dto.request.CategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toEntity(CategoryRequest categoryRequest);
}
