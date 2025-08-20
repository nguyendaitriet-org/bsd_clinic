package com.bsdclinic.repository;

import com.bsdclinic.category.CategoryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryAssignmentRepository extends JpaRepository<CategoryAssignment, String> {}