package com.bsdclinic.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class ClinicInfoValidator {
    public static class ValidRegisterTimeRangeValidator implements ConstraintValidator<ClinicInfoRuleAnnotation.ValidRegisterTimeRange, Integer> {
        @Override
        public boolean isValid(Integer registerTimeRange, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();

            if (registerTimeRange == null) {
                context.buildConstraintViolationWithTemplate("{validation.required.register_time_range}")
                        .addConstraintViolation();
                return false;
            }

            if (registerTimeRange % 5 != 0) {
                context.buildConstraintViolationWithTemplate("{validation.multiple_of_5.register_time_range}")
                        .addConstraintViolation();
                return false;
            }

            if (registerTimeRange < 5 || registerTimeRange > 60) {
                context.buildConstraintViolationWithTemplate("{validation.limit.register_time_range}")
                        .addConstraintViolation();
                return false;
            }

            return true;
        }
    }
}
