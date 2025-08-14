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
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validator {
    @RequiredArgsConstructor
    public static class UniqueEmailValidator implements ConstraintValidator<RuleAnnotation.UniqueEmail, String> {
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
            context.disableDefaultConstraintViolation();

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

    public static class ValidAvatarValidator implements ConstraintValidator<RuleAnnotation.ValidAvatar, MultipartFile> {
        private static final long MAX_AVATAR_SIZE = 2097152L;

        @Override
        public boolean isValid(MultipartFile avatar, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();

            if (avatar.isEmpty()) {
                context.buildConstraintViolationWithTemplate("{validation.required.avatar}")
                        .addConstraintViolation();
                return false;
            }

            context.disableDefaultConstraintViolation();

            String contentType = avatar.getContentType();
            if (!isSupportedContentType(contentType)) {
                context.buildConstraintViolationWithTemplate("{validation.extension.avatar}")
                        .addConstraintViolation();
                return false;
            }

            long avatarSize = avatar.getSize();
            if (avatarSize > MAX_AVATAR_SIZE) {
                context.buildConstraintViolationWithTemplate("{validation.max_size.avatar}")
                        .addConstraintViolation();
                return false;
            }

            return true;
        }

        private boolean isSupportedContentType(String contentType) {
            return contentType.equals("image/png")
                    || contentType.equals("image/jpg")
                    || contentType.equals("image/jpeg");
        }
    }

    @RequiredArgsConstructor
    public static class UniqueEmailExceptCurrentValidator implements ConstraintValidator<RuleAnnotation.UniqueEmailExceptCurrent, String> {
        private final UserRepository userRepository;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (StringUtils.isBlank(value)) {
                return true;
            }
            UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return !userRepository.existsByEmailAndUserIdNot(value, principal.getUserId());
        }
    }

    @RequiredArgsConstructor
    public static class UniquePhoneExceptCurrentValidator implements ConstraintValidator<RuleAnnotation.UniquePhoneExceptCurrent, String> {
        private final UserRepository userRepository;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (StringUtils.isBlank(value)) {
                return true;
            }
            UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return !userRepository.existsByPhoneAndUserIdNot(value, principal.getUserId());
        }
    }
}
