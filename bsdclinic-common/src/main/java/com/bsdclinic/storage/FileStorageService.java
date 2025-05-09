package com.bsdclinic.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String uploadFile(MultipartFile file, String path, String fileName);
    Resource downloadFile(String fileName, String path);
    Resource downloadFileByBaseName(String fileName, String path);
    void deleteFile(String filename, String path);
    void deleteFilesByBaseName(String filename, String path);
}
