package com.bsdclinic;

import com.bsdclinic.constant.ComponentName;
import com.bsdclinic.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service(ComponentName.S3_FILE_STORAGE)
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {
    private final MessageProvider messageProvider;
    private final S3Client s3Client;

    @Value("${storage.s3.bucket-name}")
    private String bucketName;

    @Value("${storage.s3.base-path:}")
    private String basePath;

    private String buildKey(String path, String fileName) {
        String safeBasePath = basePath == null ? "" : basePath.trim();
        String safePath = path == null ? "" : path.trim();

        return Stream.of(safeBasePath, safePath, fileName)
                .filter(part -> part != null && !part.isBlank())
                .map(part -> part.replaceAll("^/+", "").replaceAll("/+$", ""))
                .collect(Collectors.joining("/"));
    }

    @Override
    public String uploadFile(MultipartFile file, String path, String fileName) {
        String key = buildKey(path, fileName);
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return key;
        } catch (IOException e) {
            throw new RuntimeException(messageProvider.getMessage("message.file.fail_to_store", fileName), e);
        }
    }

    @Override
    public Resource downloadFile(String filename, String path) {
        String key = buildKey(path, filename);
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            return new InputStreamResource(s3Client.getObject(request));
        } catch (S3Exception e) {
            return null;
        }
    }

    @Override
    public Resource downloadFileByBaseName(String fileName, String path) {
        String prefix = buildKey(path, "");
        try {
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(listRequest);

            String matchedKey = response.contents().stream()
                    .map(S3Object::key)
                    .filter(key -> getBaseName(key.substring(key.lastIndexOf('/') + 1)).equals(fileName))
                    .findFirst()
                    .orElse(null);

            if (matchedKey == null) {
                return null;
            }

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(matchedKey)
                    .build();

            return new InputStreamResource(s3Client.getObject(getObjectRequest));
        } catch (S3Exception e) {
            return null;
        }
    }

    @Override
    public void deleteFile(String fileName, String path) {
        String key = buildKey(path, fileName);
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
        } catch (S3Exception e) {
            throw new RuntimeException(messageProvider.getMessage("message.file.fail_to_delete", fileName), e);
        }
    }

    @Override
    public void deleteFilesByBaseName(String fileName, String path) {
        String prefix = buildKey(path, "");
        try {
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(listRequest);

            List<ObjectIdentifier> toDelete = response.contents().stream()
                    .filter(obj -> getBaseName(obj.key().substring(obj.key().lastIndexOf('/') + 1)).equals(fileName))
                    .map(obj -> ObjectIdentifier.builder().key(obj.key()).build())
                    .toList();

            if (!toDelete.isEmpty()) {
                s3Client.deleteObjects(DeleteObjectsRequest.builder()
                        .bucket(bucketName)
                        .delete(d -> d.objects(toDelete))
                        .build());
            }
        } catch (S3Exception e) {
            throw new RuntimeException(messageProvider.getMessage("message.file.fail_to_delete", fileName), e);
        }
    }

    private String getBaseName(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }
}