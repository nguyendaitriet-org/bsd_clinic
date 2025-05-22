package com.bsdclinic.mapper;

import com.bsdclinic.constant.DateTimePattern;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class DateMapper {
    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern(DateTimePattern.DATE_WITH_TIME).withZone(ZoneId.systemDefault());

    public String asFormattedString(Instant instant) {
        return instant != null ? FORMATTER.format(instant) : null;
    }
}