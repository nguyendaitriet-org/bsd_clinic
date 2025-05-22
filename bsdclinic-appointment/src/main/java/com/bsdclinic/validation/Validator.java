package com.bsdclinic.validation;

import com.bsdclinic.constant.DateTimePattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class Validator {
    public static class ValidDateValidator implements ConstraintValidator<RuleAnnotation.ValidDate, String> {
        @Override
        public boolean isValid(String date, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();

            if (StringUtils.isBlank(date)) {
                context.buildConstraintViolationWithTemplate("{validation.required.date}")
                        .addConstraintViolation();
                return false;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimePattern.DEFAULT_DATE)
                    .withResolverStyle(ResolverStyle.STRICT);
            try {
                LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                context.buildConstraintViolationWithTemplate("{validation.invalid.date}")
                        .addConstraintViolation();
                return false;
            }

            return true;
        }
    }
}
