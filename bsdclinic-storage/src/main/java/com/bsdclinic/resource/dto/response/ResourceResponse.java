package com.bsdclinic.resource.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponse {
    private String resourceId;
    private String title;
    private String description;
    private String resourceType;
    private Long fileSize;
    private String createdAt;
    private String storagePath;
    private String uploadedBy;
}
