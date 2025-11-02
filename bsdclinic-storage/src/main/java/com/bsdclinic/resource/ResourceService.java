package com.bsdclinic.resource;

import com.bsdclinic.resource.dto.request.CreateResourceRequest;
import com.bsdclinic.resource.dto.request.ResourceFilter;
import com.bsdclinic.response.DatatableResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResourceService {
    void createResource(List<MultipartFile> files, CreateResourceRequest request);
    DatatableResponse getResources(ResourceFilter resourceFilter);
    void deleteResource(String resourceId);
}
