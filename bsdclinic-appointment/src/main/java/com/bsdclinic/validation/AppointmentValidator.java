package com.bsdclinic.validation;

import com.bsdclinic.constant.DateTimePattern;
import com.bsdclinic.dto.response.IUserResponse;
import com.bsdclinic.repository.UserRepository;
import com.bsdclinic.user.RoleConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppointmentValidator {
    public static class ValidRegisterDateValidator implements ConstraintValidator<AppointmentRuleAnnotation.ValidRegisterDate, String> {
        @Override
        public boolean isValid(String date, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();

            if (StringUtils.isBlank(date)) {
                context.buildConstraintViolationWithTemplate("{validation.required.register_date}")
                        .addConstraintViolation();
                return false;
            }

            LocalDate localDate;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimePattern.DEFAULT_DATE)
                    .withResolverStyle(ResolverStyle.STRICT);
            try {
                localDate = LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                context.buildConstraintViolationWithTemplate("{validation.invalid.register_date}")
                        .addConstraintViolation();
                return false;
            }

            if (localDate.isBefore(LocalDate.now().atStartOfDay().toLocalDate())) {
                context.buildConstraintViolationWithTemplate("{validation.limit.register_date}")
                        .addConstraintViolation();
                return false;
            }

            return true;
        }
    }

    public static class ValidRegisterHourValidator implements ConstraintValidator<AppointmentRuleAnnotation.ValidRegisterHour, String> {
        @Override
        public boolean isValid(String hour, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();

            if (StringUtils.isBlank(hour)) {
                context.buildConstraintViolationWithTemplate("{validation.required.register_time}")
                        .addConstraintViolation();
                return false;
            }

            LocalTime localTime;
            try {
                localTime = LocalTime.parse(hour, DateTimeFormatter.ofPattern(DateTimePattern.HOUR_MINUTE));
            } catch (DateTimeParseException e) {
                context.buildConstraintViolationWithTemplate("{validation.invalid.register_time}")
                        .addConstraintViolation();
                return false;
            }

            if (localTime.isBefore(LocalTime.now())) {
                context.buildConstraintViolationWithTemplate("{validation.limit.register_time}")
                        .addConstraintViolation();
                return false;
            }

            return true;
        }
    }

    public static class ValidBirthdayValidator implements ConstraintValidator<AppointmentRuleAnnotation.ValidBirthday, String> {
        @Override
        public boolean isValid(String birthdayString, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();

            if (StringUtils.isBlank(birthdayString)) {
                context.buildConstraintViolationWithTemplate("{validation.required.birthday}")
                        .addConstraintViolation();
                return false;
            }

            LocalDate birthdayDate;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimePattern.DEFAULT_DATE)
                    .withResolverStyle(ResolverStyle.STRICT);
            try {
                birthdayDate = LocalDate.parse(birthdayString, formatter);
            } catch (DateTimeParseException e) {
                context.buildConstraintViolationWithTemplate("{validation.invalid.birthday}")
                        .addConstraintViolation();
                return false;
            }

            if (birthdayDate.isBefore(ValidationConstant.SYSTEM_MIN_DATE) ||
                    birthdayDate.isAfter(LocalDate.now().atStartOfDay().toLocalDate())) {
                context.buildConstraintViolationWithTemplate("{validation.limit.birthday}")
                        .addConstraintViolation();
                return false;
            }

            return true;
        }
    }

    public static class ValidPhone implements ConstraintValidator<AppointmentRuleAnnotation.ValidPhone, String> {
        private static final String PHONE_REGEX = "^(\\+84|84|0)?[0-9]{9,10}$";
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.trim().isEmpty()) {
                return true;
            }
            return value.matches(PHONE_REGEX);
        }
    }

    @RequiredArgsConstructor
    public static class ExistedDoctorIdValidator implements ConstraintValidator<AppointmentRuleAnnotation.ValidDoctorId, String> {
        private final UserRepository userRepository;

        @Override
        public boolean isValid(String doctorId, ConstraintValidatorContext context) {
            context.disableDefaultConstraintViolation();

            IUserResponse user = userRepository.findByIdRole(doctorId);

            if (user == null) {
                context.buildConstraintViolationWithTemplate("{validation.no_exist.doctor_id}")
                        .addConstraintViolation();
                return false;
            }

            if (!user.getRole().equals(RoleConstant.DOCTOR.name())) {
                context.buildConstraintViolationWithTemplate("{validation.invalid.doctor}")
                        .addConstraintViolation();
                return false;
            }

            return true;
        }
    }
}
