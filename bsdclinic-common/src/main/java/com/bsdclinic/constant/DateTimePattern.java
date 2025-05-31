package com.bsdclinic.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class DateTimePattern {
    public static final String DATE_WITH_TIME = "dd/MM/yyyy HH:mm:ss";
    public static final String DEFAULT_DATE = "uuuu-MM-dd";
    public static final String HOUR_MINUTE = "HH:mm";
}
