package com.bsdclinic;

import com.bsdclinic.category.Category;
import com.bsdclinic.dto.request.CategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public void createCategories(CategoryRequest categoryRequest) {
        Category category = categoryMapper.toEntity(categoryRequest);
        categoryRepository.save(category);
    }
}
