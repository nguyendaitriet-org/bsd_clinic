package com.bsdclinic.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

public class RuleAnnotation {
    @Documented
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = Validator.ValidDateValidator.class)
    @ReportAsSingleViolation
    public @interface ValidDate {
        String message() default "Invalid Date";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}
