package com.bsdclinic.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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

    public Map<String, String> getMessageMap(String prefix, String bundleName) {
        ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
        return Collections.list(bundle.getKeys())
                .stream()
                .filter(k -> k.startsWith(prefix + '.'))
                .collect(Collectors.toMap(
                        k -> k.replace(prefix + '.', ""),
                        bundle::getString
                ));
    }
}
