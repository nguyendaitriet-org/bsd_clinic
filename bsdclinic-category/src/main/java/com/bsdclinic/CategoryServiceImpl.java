package com.bsdclinic;

import com.bsdclinic.category.Category;
import com.bsdclinic.category.CategoryAssignment;
import com.bsdclinic.dto.request.CategoryListRequest;
import com.bsdclinic.dto.request.CategoryCreateRequest;
import com.bsdclinic.dto.request.CategoryUpdateRequest;
import com.bsdclinic.dto.response.CategoryResponse;
import com.bsdclinic.dto.response.ICategoryResponse;
import com.bsdclinic.exception_handler.exception.ConflictException;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.repository.CategoryAssignmentRepository;
import com.bsdclinic.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final MessageProvider messageProvider;
    private final CategoryRepository categoryRepository;
    private final CategoryAssignmentRepository categoryAssignmentRepository;

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
        Category category = findById(categoryId);
        category.setTitle(categoryUpdateRequest.getTitle());
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category = findById(categoryId);
        Integer categoryAssignmentCount = categoryRepository.countCategoryAssignments(categoryId);
        if (categoryAssignmentCount > 0) {
            throw new ConflictException(messageProvider.getMessage("message.category.assigned"));
        }
        categoryRepository.delete(category);
    }

    @Override
    public void createCategoryAssignments(String entityId, String entityTitle, Set<String> categoryIds) {
        List<CategoryAssignment> assignments = categoryIds.stream()
                .map(categoryId -> CategoryAssignment.builder()
                        .entityId(entityId)
                        .entityTitle(entityTitle)
                        .categoryId(categoryId)
                        .build()
                ).toList();
        categoryAssignmentRepository.saveAll(assignments);
    }

    /**
     * Groups category assignments by entityId and maps each group
     * to a list of CategoryResponse objects.
     * <p>
     * Example result:
     * {
     * "entity_id_1": [CategoryResponse1, CategoryResponse2],
     * "entity_id_2": [CategoryResponse3]
     * }
     */
    @Override
    public Map<String, List<CategoryResponse>> getAssignmentsByEntityIds(List<String> entityIds) {
        return categoryAssignmentRepository.getAllByEntityIds(entityIds)
                .stream()
                .collect(Collectors.groupingBy(
                        ICategoryResponse::getEntityId,
                        Collectors.mapping(categoryMapper::toCategoryResponse, Collectors.toList())
                ));
    }

    @Override
    public void deleteAssignmentByEntityId(String entityId) {
        categoryAssignmentRepository.deleteByEntityId(entityId);
    }

    private Category findById(String categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.category"))
        );
    }

}
