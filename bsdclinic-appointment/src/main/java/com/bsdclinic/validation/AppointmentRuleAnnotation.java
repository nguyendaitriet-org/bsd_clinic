package com.bsdclinic.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

public class AppointmentRuleAnnotation {
    @Documented
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = Validator.ValidRegisterDateValidator.class)
    @ReportAsSingleViolation
    public @interface ValidRegisterDate {
        String message() default "Invalid register date";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = Validator.ValidRegisterHourValidator.class)
    @ReportAsSingleViolation
    public @interface ValidRegisterHour {
        String message() default "Invalid register hour";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = Validator.ValidBirthdayValidator.class)
    @ReportAsSingleViolation
    public @interface ValidBirthday {
        String message() default "Invalid birthday";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}
