package com.bsdclinic.validation;

import com.bsdclinic.category.CategoryType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
}
