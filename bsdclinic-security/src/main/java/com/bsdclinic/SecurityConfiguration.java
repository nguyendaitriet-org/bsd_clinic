package com.bsdclinic;

import com.bsdclinic.auth_user.AuthUserDetailService;
import com.bsdclinic.auth_user.UserJwtAuthenticationFilter;
import com.bsdclinic.error_handler.CustomAccessDeniedHandler;
import com.bsdclinic.error_handler.CustomAuthenticationEntryPoint;
import com.bsdclinic.url.WebUrl;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {
    public static final int TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 30;

    public static final String[] ENDPOINTS_WHITELIST = {
            WebUrl.CLIENT_HOME,
            WebUrl.LOGIN,
            WebUrl.API_ADMIN_LOGIN,
            "/error/**",
            "/common/**",
            "/admin/assets/**",
            "/admin/custom/**",
            "/client/assets/**",
            "/client/custom/**"
    };

    private final AuthUserDetailService authUserDetailService;
    private final UserJwtAuthenticationFilter userFilter;
    private final PasswordEncoder passwordEncoder;
    protected final MessageSource messageSource;
    protected final ObjectMapper objectMapper;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

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
                        .anyRequest()
                        .authenticated()
                )
                .exceptionHandling(configurer -> configurer
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .formLogin(configurer -> configurer
                        .loginPage(WebUrl.LOGIN)
                        .permitAll()
                )
                .logout(configurer -> configurer
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl(WebUrl.LOGIN)
                        .deleteCookies("JWT")
                        .invalidateHttpSession(true)
                );

        http.addFilterBefore(userFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}