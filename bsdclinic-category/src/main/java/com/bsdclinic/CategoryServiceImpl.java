package com.bsdclinic;

import com.bsdclinic.category.Category;
import com.bsdclinic.dto.request.CategoryListRequest;
import com.bsdclinic.dto.request.CategoryCreateRequest;
import com.bsdclinic.dto.request.CategoryUpdateRequest;
import com.bsdclinic.dto.response.CategoryResponse;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final MessageProvider messageProvider;

    @Override
    public void createCategory(CategoryCreateRequest categoryCreateRequest) {
        Category category = categoryMapper.toEntity(categoryCreateRequest);
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryResponse> getCategories(CategoryListRequest request) {
        List<Category> categories = categoryRepository.findCategoriesWithFilters(request.getKeyword(), request.getCategoryType());
        return categoryMapper.toDtos(categories);
    }

    @Override
    public void updateCategory(String categoryId, CategoryUpdateRequest categoryUpdateRequest) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.category"))
        );
        category.setTitle(categoryUpdateRequest.getTitle());
        categoryRepository.save(category);
    }
}
