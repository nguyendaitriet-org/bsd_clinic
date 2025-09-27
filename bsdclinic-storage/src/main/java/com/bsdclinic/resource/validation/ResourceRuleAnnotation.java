package com.bsdclinic.resource.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

public class ResourceRuleAnnotation {
    @Documented
    @Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = ResourceValidator.ValidResourceValidator.class)
    @ReportAsSingleViolation
    public @interface ValidResource {
        String message() default "Invalid resources";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}
