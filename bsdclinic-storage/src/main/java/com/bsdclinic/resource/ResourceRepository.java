package com.bsdclinic.resource;

import com.bsdclinic.resource.dto.response.IResourceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<AppResource, String> {
    @Query(value = """
        SELECT
            r.resource_id AS resourceId,
            r.title AS title,
            r.description AS description,
            r.resource_type AS resourceType,
            r.file_size AS fileSize,
            r.created_at AS createdAt,
            r.storage_path AS storagePath,
            u.full_name AS uploadedBy
        FROM resources r
        JOIN users u ON r.uploaded_by = u.user_id
        WHERE
             check_filter(r.title, :keyword)
             AND check_filter(r.resource_type, :resourceType)
    """, nativeQuery = true)
    Page<IResourceResponse> findResourcesWithFilter(
            @Param("keyword") String keyword,
            @Param("resourceType") String createdAtFrom,
            Pageable pageable
    );
}
