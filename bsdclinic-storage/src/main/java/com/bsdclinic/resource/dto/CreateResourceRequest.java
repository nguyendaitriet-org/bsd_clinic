package com.bsdclinic.resource.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateResourceRequest {
    @NotBlank(message = "{validation.required.resource_title}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String title;

    @NotBlank(message = "{validation.required.resource_type}")
    @Size(max = 20, message = "{validation.input.max_length.20}")
    private String resourceType;

    @Size(max = 1000, message = "{validation.input.max_length.1000}")
    private String description;
}
