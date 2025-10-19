package com.bsdclinic.resource.dto.request;

import com.bsdclinic.category.CategoryType;
import com.bsdclinic.validation.CategoryRuleAnnotation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CreateResourceRequest {
    @NotEmpty(message = "{validation.required.file_meta_data}")
    private List<@Valid ResourceMetadataDto> files;

    @NotBlank(message = "{validation.required.resource_type}")
    @Size(max = 20, message = "{validation.input.max_length.20}")
    private String resourceType;

    @Size(max = 1000, message = "{validation.input.max_length.1000}")
    private String description;

    @CategoryRuleAnnotation.ValidCategoryIds(
            message = "{validation.invalid.category_ids}",
            categoryType = CategoryType.RESOURCE
    )
    private Set<String> categoryIds;
}
