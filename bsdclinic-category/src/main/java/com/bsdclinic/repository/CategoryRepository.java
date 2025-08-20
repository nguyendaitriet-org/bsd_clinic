package com.bsdclinic.repository;

import com.bsdclinic.category.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query(value = """
            SELECT * FROM categories c WHERE 
                (:keyword IS NULL OR vietnamese_text_search(c.title, :keyword)) AND
                (:categoryType IS NULL OR c.category_type = :categoryType)
            ORDER BY c.title"""
    , nativeQuery = true)
    List<Category> findCategoriesWithFilters(String keyword, String categoryType);

    @Query(value = "SELECT COUNT(*) FROM category_assignments c WHERE c.category_id = :categoryId", nativeQuery = true)
    Integer countCategoryAssignments(String categoryId);

    @Query(value = "SELECT c.categoryId FROM Category AS c WHERE c.categoryType = :categoryType")
    Set<String> getCategoryIdsByCategoryType(String categoryType);
}