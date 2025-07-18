package com.bsdclinic.api;

import com.bsdclinic.constant.ComponentName;
import com.bsdclinic.storage.FileStorageService;
import com.bsdclinic.url.WebUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FileApi {
    @Autowired
    @Qualifier(value = ComponentName.LOCAL_FILE_STORAGE)
    private FileStorageService fileStorageService;

    @GetMapping(WebUrl.API_PUBLIC_IMAGE_BY_NAME)
    public byte[] getImageByName(@PathVariable String imageName, @RequestParam String imagePath) throws IOException {
        return fileStorageService.downloadFile(imageName, imagePath).getContentAsByteArray();
    }
}
