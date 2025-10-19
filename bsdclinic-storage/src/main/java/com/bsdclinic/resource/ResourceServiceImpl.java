package com.bsdclinic.resource;

import com.bsdclinic.BaseEntity_;
import com.bsdclinic.CategoryService;
import com.bsdclinic.FileStorageService;
import com.bsdclinic.UserPrincipal;
import com.bsdclinic.resource.dto.request.CreateResourceRequest;
import com.bsdclinic.resource.dto.request.ResourceFilter;
import com.bsdclinic.resource.dto.request.ResourceMetadataDto;
import com.bsdclinic.resource.dto.response.IResourceResponse;
import com.bsdclinic.resource.dto.response.ResourceResponse;
import com.bsdclinic.response.DatatableResponse;
import io.jsonwebtoken.lang.Collections;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;
    private final ResourceMapper resourceMapper;

    private final String resourceRootPath = "resources";

    @Override
    @Transactional
    public void createResource(List<MultipartFile> files, CreateResourceRequest request) {
        /* Save files to storage */
        String storagePath = resourceRootPath + "/" + request.getResourceType();
        Map<String, String> resourcePathMap = fileStorageService.uploadFiles(files, storagePath);

        try {
            saveResourcesWithCategories(resourcePathMap, request);
        } catch (Exception e) {
            rollbackFileUploads(resourcePathMap, storagePath);
            throw e;
        }
    }

    private void saveResourcesWithCategories(Map<String, String> resourcePathMap, CreateResourceRequest request) {
        /* Save AppResource to database */
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getUserId();
        List<ResourceMetadataDto> resourceMetadataDtos = request.getFiles();
        List<AppResource> appResources = resourceMetadataDtos.stream().map(fileMetaData -> {
            AppResource appResource = new AppResource();
            String resourceId = fileMetaData.getResourceId();
            appResource.setResourceId(resourceId);
            appResource.setTitle(fileMetaData.getTitle());
            appResource.setResourceType(request.getResourceType());
            appResource.setDescription(request.getDescription());
            appResource.setMimeType(fileMetaData.getMimeType());
            appResource.setFileSize(fileMetaData.getFileSize());
            appResource.setStoragePath(resourcePathMap.get(resourceId));
            appResource.setUploadedBy(userId);
            return appResource;
        }).toList();
        resourceRepository.saveAll(appResources);

        /* Save CategoryAssignment to database */
        Set<String> categoryIds = request.getCategoryIds();
        if (!Collections.isEmpty(categoryIds)) {
            resourceMetadataDtos.forEach(fileMetaData ->
                    categoryService.createCategoryAssignments(
                            fileMetaData.getResourceId(),
                            fileMetaData.getTitle(),
                            categoryIds
                    )
            );
        }
    }

    private void rollbackFileUploads(Map<String, String> resourcePathMap, String storagePath) {
        for (var path : resourcePathMap.entrySet()) {
            fileStorageService.deleteFilesByBaseName(path.getKey(), storagePath);
        }
    }

    @Override
    public DatatableResponse getResources(ResourceFilter resourceFilter) {
        Pageable pageable = PageRequest.of(
                resourceFilter.getStart() / resourceFilter.getLength(),
                resourceFilter.getLength(),
                Sort.by(Sort.Direction.DESC, BaseEntity_.CREATED_AT)
        );
        Page<IResourceResponse> resourcesPage = resourceRepository.findResourcesWithFilter(
                resourceFilter.getKeyword(),
                resourceFilter.getResourceType(),
                pageable
        );

        DatatableResponse<ResourceResponse> datatableResponse = new DatatableResponse<>();
        datatableResponse.setData(resourceMapper.toResourceListResponses(resourcesPage.getContent()));
        datatableResponse.setDraw(resourceFilter.getDraw());
        Long totalRecord = resourcesPage.getTotalElements();
        datatableResponse.setRecordsFiltered(totalRecord);
        datatableResponse.setRecordsTotal(totalRecord);

        return datatableResponse;
    }
}
