package com.bsdclinic.validation;

import com.bsdclinic.UserPrincipal;
import com.bsdclinic.repository.RoleRepository;
import com.bsdclinic.repository.UserRepository;
import com.bsdclinic.user.UserStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public static class NotExistedUserIdValidator implements ConstraintValidator<RuleAnnotation.NotExistedUserId, String> {
        private final UserRepository userRepository;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return Boolean.TRUE.equals(userRepository.existsByUserId(value));
        }
    }

    public static class NotExistedUserStatusValidator implements ConstraintValidator<RuleAnnotation.NotExistedUserStatus, String> {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return UserStatus.getAllNames().contains(value);
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
            if (StringUtils.isBlank(value)) {
                context.buildConstraintViolationWithTemplate("{validation.required.old_password}").addConstraintViolation();
                return false;
            }

            UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!passwordEncoder.matches(value, principal.getPassword())) {
                context.buildConstraintViolationWithTemplate("{validation.no_match.old_password}").addConstraintViolation();
                return false;
            }

            return true;
        }
    }
}
