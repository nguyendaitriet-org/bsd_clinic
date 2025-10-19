package com.bsdclinic.resource.dto.response;

public interface IResourceResponse {
    String getResourceId();
    String getTitle();
    String getDescription();
    String getResourceType();
    Long getFileSize();
    String getCreatedAt();
    String getStoragePath();
    String getUploadedBy();
}
