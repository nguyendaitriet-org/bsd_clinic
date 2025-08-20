package com.bsdclinic.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryAssignmentRequest {
    private String categoryId;
    private String entityId;
    private String entityTitle;
}
