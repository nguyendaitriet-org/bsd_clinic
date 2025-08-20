package com.bsdclinic.validation;

import com.bsdclinic.category.CategoryType;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.repository.CategoryRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MedicineValidator {
    @RequiredArgsConstructor
    public static class ValidCategoryIdsValidator implements ConstraintValidator<MedicineRuleAnnotation.ValidCategoryIds, Set<String>> {
        private final CategoryRepository categoryRepository;

        @Override
        public boolean isValid(Set<String> categoryIds, ConstraintValidatorContext context) {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return true;
            }

            Set<String> medicineCategoryIds = categoryRepository.getCategoryIdsByCategoryType(CategoryType.MEDICINE.name());
            return medicineCategoryIds.containsAll(categoryIds);
        }
    }

    @RequiredArgsConstructor
    public static class ValidUnitValidator implements ConstraintValidator<MedicineRuleAnnotation.ValidUnit, String> {
        private final MessageProvider messageProvider;

        @Override
        public boolean isValid(String medicineUnit, ConstraintValidatorContext context) {
            if (StringUtils.isBlank(medicineUnit)) {
                return true;
            }

            Map<String, String> unitMap = messageProvider.getMessageMap("medicine.dosage_unit", "constants");
            return unitMap.containsKey(medicineUnit);
        }
    }
}
