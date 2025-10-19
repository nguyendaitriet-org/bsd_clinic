package com.bsdclinic.resource;

import com.bsdclinic.BaseEntity;import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    /* Unit: bytes */
    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "storage_path")
    private String storagePath;

    /* User ID */
    @Column(name = "uploaded_by")
    private String uploadedBy;
}
