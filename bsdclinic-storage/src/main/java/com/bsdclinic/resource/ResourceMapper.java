package com.bsdclinic.resource;

import com.bsdclinic.resource.dto.response.IResourceResponse;
import com.bsdclinic.resource.dto.response.ResourceResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResourceMapper {
    ResourceResponse toResourceResponse(IResourceResponse resourceResponse);
    List<ResourceResponse> toResourceListResponses(List<IResourceResponse> resourceResponses);
}