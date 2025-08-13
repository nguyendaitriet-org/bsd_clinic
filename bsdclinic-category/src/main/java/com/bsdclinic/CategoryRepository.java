package com.bsdclinic;

import com.bsdclinic.category.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

}