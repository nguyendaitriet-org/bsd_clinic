package com.bsdclinic.repository;

import com.bsdclinic.category.CategoryAssignment;
import com.bsdclinic.dto.response.ICategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryAssignmentRepository extends JpaRepository<CategoryAssignment, String> {
    @Query("SELECT " +
                "a.entityId AS entityId, " +
                "a.categoryId AS categoryId, " +
                "c.title AS title " +
           "FROM CategoryAssignment AS a " +
           "INNER JOIN Category AS c ON a.categoryId = c.categoryId " +
           "WHERE a.entityId IN :entityIds")
    List<ICategoryResponse> getAllByEntityIds(List<String> entityIds);
}