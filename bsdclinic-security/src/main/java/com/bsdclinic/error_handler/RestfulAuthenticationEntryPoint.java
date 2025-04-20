package com.bsdclinic.error_handler;

import com.bsdclinic.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class RestfulAuthenticationEntryPoint implements AuthenticationEntryPoint {
    protected final MessageSource messageSource;
    protected final ObjectMapper objectMapper;

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        String message = this.messageSource.getMessage("error.401", new Object[0], LocaleContextHolder.getLocale());
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
    public RestfulAuthenticationEntryPoint(final MessageSource messageSource, final ObjectMapper objectMapper) {
        this.messageSource = messageSource;
        this.objectMapper = objectMapper;
    }
}