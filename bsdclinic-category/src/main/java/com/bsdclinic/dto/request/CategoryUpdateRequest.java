package com.bsdclinic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateRequest {
    @NotBlank(message = "{validation.required.category_title}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String title;
}
