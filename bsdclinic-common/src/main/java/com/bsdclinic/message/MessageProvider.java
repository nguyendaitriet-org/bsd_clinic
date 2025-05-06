package com.bsdclinic.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageProvider {
    private final MessageSource messageSource;

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String key, String... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public Map<String, String> getMessageMap(String prefix, List<String> messageKeys) {
        Map<String, String> roleMap = new LinkedHashMap<>();
        for (String messageKey : messageKeys) {
            String key = prefix + '.' + messageKey;
            String value = messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
            roleMap.put(messageKey, value);
        }

        return roleMap;
    }
}
