package com.bsdclinic.validation;

import com.bsdclinic.RoleRepository;
import com.bsdclinic.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

public class Validator {
    @RequiredArgsConstructor
    public static class ExistedEmailValidator implements ConstraintValidator<RuleAnnotation.ExistedEmail, String> {
        private final UserRepository userRepository;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return Boolean.TRUE.equals(userRepository.existsByEmail(value));
        }
    }

    @RequiredArgsConstructor
    public static class NotExistedRoleValidator implements ConstraintValidator<RuleAnnotation.NotExistedRole, String> {
        private final RoleRepository roleRepository;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return Boolean.TRUE.equals(roleRepository.existsByRoleId(value));
        }
    }
}
