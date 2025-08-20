package com.bsdclinic.dto.request;

import com.bsdclinic.validation.CategoryRuleAnnotation;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryListRequest {
    private String keyword;

    @CategoryRuleAnnotation.ValidCategoryType(message = "{validation.invalid.category_type}")
    private String categoryType;
}
