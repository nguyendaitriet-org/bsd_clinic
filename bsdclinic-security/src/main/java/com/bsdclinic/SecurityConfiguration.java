package com.bsdclinic;

import com.bsdclinic.auth_user.AuthUserDetailService;
import com.bsdclinic.auth_user.UserJwtAuthenticationFilter;
import com.bsdclinic.error_handler.RestfulAccessDeniedHandler;
import com.bsdclinic.error_handler.RestfulAuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.RequestMatcherDelegatingAccessDeniedHandler;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {
    public static final int TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 30;

    public static final String[] ENDPOINTS_WHITELIST = {
            /*-------------[TEST]-------------*/
            "/api/users/test-creation",
            /*--------------------------------*/
            "/api/login",
            "/error/**",
            "/css/**",
            "/js/**",
            "/img/**",
            "/messages/**"
    };

    private final AuthUserDetailService authUserDetailService;
    private final UserJwtAuthenticationFilter userFilter;
    private final PasswordEncoder passwordEncoder;
    protected final MessageSource messageSource;
    protected final ObjectMapper objectMapper;

    @Configuration
    public static class PasswordEncoderConfiguration {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(10);
        }
    }

    @Bean(SecurityBeanName.USER_DAO_AUTHENTICATION_PROVIDER)
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(authUserDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper() {
        // Use SimpleAuthorityMapper to remove the ROLE_ prefix
        SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
        authorityMapper.setConvertToUpperCase(false); // To maintain case sensitivity
        return authorityMapper;
    }

    public AuthenticationEntryPoint authenticationEntryPoint() {
        LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>() {{
            put(new AntPathRequestMatcher("/api/**"), new RestfulAuthenticationEntryPoint(messageSource, objectMapper));
        }};
        DelegatingAuthenticationEntryPoint defaultEntryPoint = new DelegatingAuthenticationEntryPoint(entryPoints);
        defaultEntryPoint.setDefaultEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
        return defaultEntryPoint;
    }

    public AccessDeniedHandler accessDeniedHandler() {
        LinkedHashMap<RequestMatcher, AccessDeniedHandler> handlers = new LinkedHashMap<>() {{
            put(new AntPathRequestMatcher("/api/**"), new RestfulAccessDeniedHandler(messageSource, objectMapper));
        }};
        AccessDeniedHandler defaultHandler = new AccessDeniedHandlerImpl();
        return new RequestMatcherDelegatingAccessDeniedHandler(handlers, defaultHandler);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(configurer -> configurer.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(ENDPOINTS_WHITELIST)
                        .permitAll()
                        .requestMatchers("/admin/**")
                        .authenticated()
                )
                .exceptionHandling(configurer -> configurer
                        .accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(authenticationEntryPoint())
                )
                .formLogin(configurer -> configurer
                        .loginPage("/admin/login")
                        .permitAll()
                )
                .logout(configurer -> configurer
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/admin/login")
                        .deleteCookies("JWT")
                        .invalidateHttpSession(true)
                );

        http.addFilterBefore(userFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}