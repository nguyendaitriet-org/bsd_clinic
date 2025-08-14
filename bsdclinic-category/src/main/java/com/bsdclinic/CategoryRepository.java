package com.bsdclinic;

import com.bsdclinic.category.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query(value = """
            SELECT * FROM categories c WHERE 
                (:keyword IS NULL OR vietnamese_text_search(c.title, :keyword)) AND
                (:categoryType IS NULL OR c.category_type = :categoryType)"""
    , nativeQuery = true)
    List<Category> findCategoriesWithFilters(String keyword, String categoryType);
}