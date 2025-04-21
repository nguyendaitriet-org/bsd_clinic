package com.bsdclinic;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RoleAuthorization {
    public static final String CODE_ADMIN = "ADMIN";
    public static final String CODE_DOCTOR = "DOCTOR";
    public static final String CODE_STAFF = "STAFF";
    public static final String PREFIX = "hasAnyAuthority('";
    public static final String SUFFIX = "')";
    public static final String MIDDLE = "','";

    public static final String ADMIN_DOCTOR_AUTHORIZATION = PREFIX + CODE_ADMIN + MIDDLE + CODE_DOCTOR + SUFFIX;
    public static final String ADMIN_AUTHORIZATION = PREFIX + CODE_ADMIN + SUFFIX;
    public static final String DOCTOR_AUTHORIZATION = PREFIX + CODE_DOCTOR + SUFFIX;
    public static final String STAFF_AUTHORIZATION = PREFIX + CODE_STAFF + SUFFIX;
    public static final String AUTHENTICATED = "isAuthenticated()";

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize(ADMIN_DOCTOR_AUTHORIZATION)
    public @interface AdminAndDoctorAuthorization {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize(ADMIN_AUTHORIZATION)
    public @interface AdminAuthorization {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize(DOCTOR_AUTHORIZATION)
    public @interface DoctorAuthorization {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize(STAFF_AUTHORIZATION)
    public @interface StaffAuthorization {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize(AUTHENTICATED)
    public @interface AuthenticatedUser {
    }
}
