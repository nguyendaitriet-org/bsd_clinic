package com.bsdclinic.resource.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceMetadataDto {
    @NotBlank(message = "{validation.required.resource_id}")
    @Size(max = 50, message = "{validation.input.max_length.50}")
    private String resourceId;

    @NotBlank(message = "{validation.required.resource_title}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String title;

    @NotBlank(message = "{validation.required.resource_mime_type}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String mimeType;

    @NotNull
    private Long fileSize;
}
