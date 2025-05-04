package com.bsdclinic.validation;

import com.bsdclinic.RoleRepository;
import com.bsdclinic.UserPrincipal;
import com.bsdclinic.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @RequiredArgsConstructor
    public static class CheckOldPasswordValidator implements ConstraintValidator<RuleAnnotation.CheckOldPassword, String> {
        private final PasswordEncoder passwordEncoder;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            /* Return true when the value is empty in order to avoid merging the same attribute error */
            if (value.isEmpty()) return true;

            UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            passwordEncoder.matches(value, principal.getPassword());
            return passwordEncoder.matches(value, principal.getPassword());
        }
    }
}
