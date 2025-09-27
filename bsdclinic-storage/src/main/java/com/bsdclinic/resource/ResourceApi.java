package com.bsdclinic.resource;

import com.bsdclinic.FileStorageService;
import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.constant.ComponentName;
import com.bsdclinic.resource.dto.CreateResourceRequest;
import com.bsdclinic.resource.validation.ResourceRuleAnnotation;
import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ResourceApi {
    @Autowired
    @Qualifier(value = ComponentName.S3_FILE_STORAGE)
    private FileStorageService fileStorageService;

    @Autowired
    private ResourceService resourceService;

    @GetMapping(WebUrl.API_PUBLIC_IMAGE_BY_NAME)
    public byte[] getImageByName(@PathVariable String imageName, @RequestParam String imagePath) throws IOException {
        return fileStorageService.downloadFile(imageName, imagePath).getContentAsByteArray();
    }

    @RoleAuthorization.AuthenticatedUser
    @PostMapping(value = WebUrl.API_ADMIN_RESOURCE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void storeResources(
            @RequestPart("files") @Valid @ResourceRuleAnnotation.ValidResource List<MultipartFile> files,
            @RequestPart("data") @Valid CreateResourceRequest data
    ) {
        resourceService.createResource(files, data);
    }
}
