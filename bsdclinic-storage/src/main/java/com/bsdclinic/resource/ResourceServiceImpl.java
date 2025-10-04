package com.bsdclinic.resource;

import com.bsdclinic.FileStorageService;
import com.bsdclinic.constant.ComponentName;
import com.bsdclinic.resource.dto.CreateResourceRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;

    @Autowired
    @Qualifier(ComponentName.S3_FILE_STORAGE)
    private FileStorageService fileStorageService;

    private final String resourceRootPath = "resources";

    @Override
    @Transactional
    public void createResource(List<MultipartFile> files, CreateResourceRequest request) {
        String filePath = resourceRootPath + "/" + request.getResourceType();
        Map<String, String> resourcePathMap = fileStorageService.uploadFiles(files, filePath);
        List<AppResource> appResources = new ArrayList<>();

    }
}
