package com.bsdclinic.validation;

import com.bsdclinic.category.CategoryType;
import com.bsdclinic.repository.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryValidator {
    public static class ValidCategoryTypeValidator implements ConstraintValidator<CategoryRuleAnnotation.ValidCategoryType, String> {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (StringUtils.isBlank(value)) {
                return true;
            }
            List<String> categoryTypes = CategoryType.getAllNames();
            return categoryTypes.contains(value);
        }
    }

    @RequiredArgsConstructor
    public static class ValidCategoryIdsValidator implements ConstraintValidator<CategoryRuleAnnotation.ValidCategoryIds, Set<String>> {
        private final CategoryRepository categoryRepository;

        private CategoryType categoryType;

        @Override
        public void initialize(CategoryRuleAnnotation.ValidCategoryIds constraintAnnotation) {
            this.categoryType = constraintAnnotation.categoryType();
        }

        @Override
        public boolean isValid(Set<String> categoryIds, ConstraintValidatorContext context) {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return true;
            }

            Set<String> medicineCategoryIds = categoryRepository.getCategoryIdsByCategoryType(categoryType.name());
            return medicineCategoryIds.containsAll(categoryIds);
        }
    }
}
