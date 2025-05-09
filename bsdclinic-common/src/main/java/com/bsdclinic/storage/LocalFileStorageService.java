package com.bsdclinic.storage;

import com.bsdclinic.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("localFileStorage")
@RequiredArgsConstructor
public class LocalFileStorageService implements FileStorageService {
    private final MessageProvider messageProvider;

    @Value("${storage.local.base-path}")
    private String basePath;

    @Override
    public String uploadFile(MultipartFile file, String path, String fileName) {
        try {
            Path destination = Paths.get(basePath, path, fileName);
            Files.createDirectories(destination.getParent());
            file.transferTo(destination);
            return destination.toString();
        } catch (IOException e) {
            throw new RuntimeException(messageProvider.getMessage("message.file.fail_to_store"), e);
        }
    }

    @Override
    public Resource downloadFile(String filename, String path) {
        try {
            Path filePath = Paths.get(basePath, path, filename).normalize();
            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                return null;
            }
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(messageProvider.getMessage("message.file.invalid_path"), e);
        }
    }

    @Override
    public void deleteFile(String filename, String path) {
        try {
            Path filePath = Paths.get(basePath, path, filename).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(messageProvider.getMessage("message.file.fail_to_delete"), e);
        }
    }
}