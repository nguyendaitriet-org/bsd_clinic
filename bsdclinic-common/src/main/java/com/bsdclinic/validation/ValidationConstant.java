package com.bsdclinic.validation;

import java.time.LocalDate;

public class ValidationConstant {
    public static final String EMAIL_PATTERN = "[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$";
    public static final String GENDER_PATTERN = "MALE|FEMALE";
    public static final LocalDate SYSTEM_MIN_DATE = LocalDate.of(1900, 1, 1);
}
