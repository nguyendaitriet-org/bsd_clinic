package com.bsdclinic.validation;

import com.bsdclinic.repository.RoleRepository;
import com.bsdclinic.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class Validator {
    @RequiredArgsConstructor
    public static class ExistedEmailValidator implements ConstraintValidator<RuleAnnotation.ExistedEmail, String> {
        private final UserRepository userRepository;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return Boolean.FALSE.equals(userRepository.existsByEmail(value));
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

    @RequiredArgsConstructor
    public static class ExistedPhoneValidator implements ConstraintValidator<RuleAnnotation.ExistedPhone, String> {
        private final UserRepository userRepository;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return Boolean.FALSE.equals(userRepository.existsByPhone(value));
        }
    }
}
