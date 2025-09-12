package com.bsdclinic;

import com.bsdclinic.constant.ComponentName;
import com.bsdclinic.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service(ComponentName.LOCAL_FILE_STORAGE)
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
            throw new RuntimeException(messageProvider.getMessage("message.file.fail_to_store", fileName), e);
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
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Resource downloadFileByBaseName(String fileName, String path) {
        Path dirPath = Paths.get(basePath, path).normalize();
        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            return null;
        }
        try (Stream<Path> paths = Files.list(dirPath)) {
            Optional<Path> matchedFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> getBaseName(p.getFileName().toString()).equals(fileName))
                    .findFirst();

            if (matchedFiles.isEmpty()) {
                return null;
            }
            return new UrlResource(matchedFiles.get().toUri());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFile(String fileName, String path) {
        try {
            Path filePath = Paths.get(basePath, path, fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(messageProvider.getMessage("message.file.fail_to_delete", fileName), e);
        }
    }

    @Override
    public void deleteFilesByBaseName(String fileName, String path) {
        Path dirPath = Paths.get(basePath, path).normalize();
        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            return;
        }
        try (Stream<Path> paths = Files.list(dirPath)) {
            List<Path> matchedFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> getBaseName(p.getFileName().toString()).equals(fileName))
                    .toList();

            if (matchedFiles.isEmpty()) {
                return;
            }

            for (Path file : matchedFiles) {
                Files.delete(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(messageProvider.getMessage("message.file.fail_to_delete", fileName));
        }
    }

    private String getBaseName(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }
}