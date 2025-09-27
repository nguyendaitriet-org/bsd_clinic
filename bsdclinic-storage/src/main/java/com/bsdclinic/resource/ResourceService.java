package com.bsdclinic.resource;

import com.bsdclinic.resource.dto.CreateResourceRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResourceService {
    void createResource(List<MultipartFile> files, CreateResourceRequest request);
}
