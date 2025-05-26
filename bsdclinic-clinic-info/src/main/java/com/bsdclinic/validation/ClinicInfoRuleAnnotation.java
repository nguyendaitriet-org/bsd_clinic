package com.bsdclinic.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

public class ClinicInfoRuleAnnotation {
    @Documented
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = ClinicInfoValidator.ValidRegisterTimeRangeValidator.class)
    @ReportAsSingleViolation
    public @interface ValidRegisterTimeRange {
        String message() default "Invalid register time range";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}
