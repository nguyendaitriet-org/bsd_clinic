package com.bsdclinic.resource;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.bsdclinic.BaseEntity;import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "resources")
public class AppResource extends BaseEntity {
    @Id
    @Column(name = "resource_id")
    private String resourceId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "resource_type")
    private String resourceType;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "storage_path")
    private String storagePath;

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @PrePersist
    public void prePersist() {
        if (resourceId == null) {
            resourceId = NanoIdUtils.randomNanoId();
        }
    }
}
