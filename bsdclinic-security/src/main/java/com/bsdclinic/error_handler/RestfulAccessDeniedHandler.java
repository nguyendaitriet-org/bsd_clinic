package com.bsdclinic.error_handler;

import com.bsdclinic.response.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    protected final MessageSource messageSource;
    protected final ObjectMapper objectMapper;

    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        String message = this.messageSource.getMessage("error.403", new Object[0], LocaleContextHolder.getLocale());
        if (log.isDebugEnabled()) {
            log.debug("Request uri:{} - status code:{} - {}", request.getRequestURI(), response.getStatus(), message);
        }

        if (log.isErrorEnabled()) {
            log.error(ex.getMessage(), ex);
        }

        Result<?> result = Result.fail().message(message).build();
        ServletOutputStream out = response.getOutputStream();
        this.objectMapper.writeValue(out, result);
        out.flush();
    }

    @Generated
    public RestfulAccessDeniedHandler(final MessageSource messageSource, final ObjectMapper objectMapper) {
        this.messageSource = messageSource;
        this.objectMapper = objectMapper;
    }
}