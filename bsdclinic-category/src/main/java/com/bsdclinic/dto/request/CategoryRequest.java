package com.bsdclinic.dto.request;

import com.bsdclinic.validation.CategoryRuleAnnotation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    @NotBlank(message = "{validation.required.category_title}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String title;

    @NotBlank(message = "{validation.required.category_type}")
    @CategoryRuleAnnotation.ValidCategoryType(message = "{validation.invalid.category_type}")
    private String categoryType;
}
