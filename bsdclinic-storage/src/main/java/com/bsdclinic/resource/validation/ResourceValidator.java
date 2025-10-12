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

            String baseExtension = null;

            for (MultipartFile resource : resources) {
                String fileName = resource.getOriginalFilename();

                if (fileName == null || !fileName.contains(".")) {
                    context.buildConstraintViolationWithTemplate("{validation.invalid.resource_file}")
                            .addConstraintViolation();
                    return false;
                }

                // Extract file extension
                String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

                // Check if first file's extension
                if (baseExtension == null) {
                    baseExtension = extension;
                } else if (!baseExtension.equals(extension)) {
                    context.buildConstraintViolationWithTemplate("{validation.same_extension.resource_file}")
                            .addConstraintViolation();
                    return false;
                }

                // Check file size
                if (resource.getSize() > MAX_RESOURCE_SIZE) {
                    context.buildConstraintViolationWithTemplate("{validation.max_size.resource_file}")
                            .addConstraintViolation();
                    return false;
                }
            }

            return true;
        }
    }
}
