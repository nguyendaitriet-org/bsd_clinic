package com.bsdclinic;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileStorageService {
    /**
     * Uploads multiple files asynchronously to the given path.
     * <p>
     * Returns a map where the key is the original file name without extension
     * and the value is the uploaded resource path.
     * </p>
     *
     * @param files list of files to upload
     * @param path  target storage path
     * @return map of file name (without extension) to uploaded resource path
     */
    Map<String, String> uploadFiles(List<MultipartFile> files, String path);

    String uploadSingleFile(MultipartFile file, String path, String fileName);

    Resource downloadFile(String fileName, String path);

    Resource downloadFileByBaseName(String fileName, String path);

    void deleteFile(String filename, String path);

    void deleteFilesByBaseName(String filename, String path);
}
