package com.bsdclinic.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

public class RuleAnnotation {
    @Documented
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = Validator.ExistedEmailValidator.class)
    @ReportAsSingleViolation
    public @interface ExistedEmail {
        String message() default "Email exists";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
    @Documented
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = Validator.NotExistedRoleValidator.class)
    @ReportAsSingleViolation
    public @interface NotExistedRole {
        String message() default "Role doesn't exist";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
    @Documented
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = Validator.ExistedPhoneValidator.class)
    @ReportAsSingleViolation
    public @interface ExistedPhone {
        String message() default "Phone` exist";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
    @Documented
    @Target({ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = Validator.CheckOldPasswordValidator.class)
    @ReportAsSingleViolation
    public @interface CheckOldPassword {
        String message() default "Incorrect old password value";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }
}
