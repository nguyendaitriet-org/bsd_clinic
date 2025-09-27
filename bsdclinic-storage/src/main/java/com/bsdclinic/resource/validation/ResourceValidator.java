package com.bsdclinic.resource.validation;

import io.jsonwebtoken.lang.Collections;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceValidator {
    public static class ValidResourceValidator implements ConstraintValidator<ResourceRuleAnnotation.ValidResource, List<MultipartFile>> {
        private static final long MAX_RESOURCE_SIZE = 2097152L;

        @Override
        public boolean isValid(List<MultipartFile> resources, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();

            if (Collections.isEmpty(resources)) {
                context.buildConstraintViolationWithTemplate("{validation.required.resource_file}")
                        .addConstraintViolation();
                return false;
            }

            for (MultipartFile resource : resources) {
                long resourceSize = resource.getSize();
                if (resourceSize > MAX_RESOURCE_SIZE) {
                    context.buildConstraintViolationWithTemplate("{validation.max_size.resource_file}")
                            .addConstraintViolation();
                    return false;
                }
            }

            return true;
        }
    }
}
