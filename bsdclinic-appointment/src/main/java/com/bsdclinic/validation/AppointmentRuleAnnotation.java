package com.bsdclinic.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

public class AppointmentRuleAnnotation {
    @Documented
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = AppointmentValidator.ValidRegisterDateValidator.class)
    @ReportAsSingleViolation
    public @interface ValidRegisterDate {
        String message() default "Invalid register date";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = AppointmentValidator.ValidRegisterHourValidator.class)
    @ReportAsSingleViolation
    public @interface ValidRegisterHour {
        String message() default "Invalid register hour";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = AppointmentValidator.ValidBirthdayValidator.class)
    @ReportAsSingleViolation
    public @interface ValidBirthday {
        String message() default "Invalid birthday";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    @Documented
    @Target({ElementType.PARAMETER, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = AppointmentValidator.ExistedDoctorIdValidator.class)
    @ReportAsSingleViolation
    public @interface ValidDoctorId {
        String message() default "Invalid doctor";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}
