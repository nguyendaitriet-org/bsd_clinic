package com.bsdclinic.mapper;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class DateMapper {
    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

    public String asFormattedString(Instant instant) {
        return instant != null ? FORMATTER.format(instant) : null;
    }
}