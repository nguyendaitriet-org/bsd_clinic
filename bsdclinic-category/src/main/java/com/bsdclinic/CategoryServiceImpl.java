package com.bsdclinic;

import com.bsdclinic.category.Category;
import com.bsdclinic.dto.request.CategoryListRequest;
import com.bsdclinic.dto.request.CategoryRequest;
import com.bsdclinic.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<CategoryResponse> getCategories(CategoryListRequest request) {
        List<Category> categories = categoryRepository.findCategoriesWithFilters(request.getKeyword(), request.getCategoryType());
        return categoryMapper.toDtos(categories);
    }
}
