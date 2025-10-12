package com.bsdclinic;

import com.bsdclinic.constant.ComponentName;
import com.bsdclinic.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service(ComponentName.S3_FILE_STORAGE)
@RequiredArgsConstructor
@Primary
public class S3FileStorageService implements FileStorageService {
    private final MessageProvider messageProvider;
    private final S3Client s3Client;

    @Value("${storage.s3.bucket-name}")
    private String bucketName;

    @Value("${storage.s3.base-path:}")
    private String basePath;

    @Override
    public Map<String, String> uploadFiles(List<MultipartFile> files, String path) {
        List<CompletableFuture<Map.Entry<String, String>>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    String originalFilename = file.getOriginalFilename();
                    String nameWithoutExt = originalFilename != null && originalFilename.contains(".")
                            ? originalFilename.substring(0, originalFilename.lastIndexOf("."))
                            : originalFilename;

                    String resourcePath = uploadSingleFile(file, path, originalFilename);
                    return Map.entry(nameWithoutExt, resourcePath);
                }))
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public String uploadSingleFile(MultipartFile file, String path, String fileName) {
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

    /**
     * Deletes all files in the given path that share the same base name (file name without extension).
     * <p>
     * This method:
     * <ul>
     *   <li>Builds a storage prefix from the given {@code path}.</li>
     *   <li>Lists all objects under that prefix in the configured S3 bucket.</li>
     *   <li>Filters objects whose base name (excluding extension) matches the given {@code fileName}.</li>
     *   <li>Deletes all matching objects in a single batch request.</li>
     * </ul>
     *
     * <p><b>Example:</b></p>
     * If the bucket contains:
     * <pre>
     * resources/user1/report.pdf
     * resources/user1/report.docx
     * resources/user1/photo.png
     * </pre>
     * Calling:
     * <pre>
     * deleteFilesByBaseName("report", "resources/user1");
     * </pre>
     * will delete both {@code report.pdf} and {@code report.docx}, but keep {@code photo.png}.
     *
     * @param fileName base file name (without extension) to match for deletion
     * @param path     S3 folder path under which to search for matching files
     * @throws RuntimeException if deletion fails due to an S3 error
     */
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

    /**
     * Extracts the base name (file name without any extensions) from the given file name.
     * <p>
     * Removes everything after the first dot ('.').
     * If the file name does not contain a dot, the original name is returned.
     * </p>
     *
     * <p><b>Examples:</b></p>
     * <ul>
     *   <li>{@code "document.pdf"} → {@code "document"}</li>
     *   <li>{@code "archive.tar.gz"} → {@code "archive"}</li>
     *   <li>{@code "readme"} → {@code "readme"}</li>
     * </ul>
     *
     * @param filename the full file name (with or without extensions)
     * @return the file name without any extensions
     */
    private String getBaseName(String filename) {
        int dotIndex = filename.indexOf('.');
        return (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
    }

    /**
     * Builds a normalized storage key by concatenating the base path, a given path, and the file name.
     * <p>
     * - Trims leading and trailing slashes from each part. <br>
     * - Ignores {@code null} or blank values. <br>
     * - Joins non-empty parts with {@code "/"}.
     * </p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * basePath = "uploads"
     * path     = "/user/docs/"
     * fileName = "file.docx"
     *
     * Result: "uploads/user/docs/file.docx"
     * </pre>
     *
     * @param path     additional path (may be {@code null} or blank)
     * @param fileName file name to append (must not be {@code null} or blank)
     * @return a normalized key string suitable for storage paths
     */
    private String buildKey(String path, String fileName) {
        String safeBasePath = basePath == null ? "" : basePath.trim();
        String safePath = path == null ? "" : path.trim();

        return Stream.of(safeBasePath, safePath, fileName)
                .filter(part -> part != null && !part.isBlank())
                .map(part -> part.replaceAll("^/+", "").replaceAll("/+$", ""))
                .collect(Collectors.joining("/"));
    }
}