package com.bsdclinic.resource.dto.request;

import com.bsdclinic.request.DatatablePagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceFilter extends DatatablePagination {
    private String keyword;
    private String resourceType;
}
