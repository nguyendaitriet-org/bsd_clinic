package com.bsdclinic.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

public class MedicineRuleAnnotation {
    @Documented
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = MedicineValidator.ValidCategoryIdsValidator.class)
    @ReportAsSingleViolation
    public @interface ValidCategoryIds {
        String message() default "Invalid category IDs";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = MedicineValidator.ValidUnitValidator.class)
    @ReportAsSingleViolation
    public @interface ValidUnit {
        String message() default "Invalid unit";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}
