package com.bsdclinic.validation;

import com.bsdclinic.category.CategoryType;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

public class CategoryRuleAnnotation {
    @Documented
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = CategoryValidator.ValidCategoryTypeValidator.class)
    @ReportAsSingleViolation
    public @interface ValidCategoryType {
        String message() default "Invalid category type";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = CategoryValidator.ValidCategoryIdsValidator.class)
    @ReportAsSingleViolation
    public @interface ValidCategoryIds {
        String message() default "Invalid category IDs";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};

        CategoryType categoryType();
    }
}
