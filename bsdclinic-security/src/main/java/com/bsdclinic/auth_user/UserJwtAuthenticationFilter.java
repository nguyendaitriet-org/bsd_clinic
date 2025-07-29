package com.bsdclinic.auth_user;

import com.bsdclinic.SecurityBeanName;

import com.bsdclinic.UserPrincipal;
import com.bsdclinic.error_handler.CustomAuthenticationEntryPoint;
import com.bsdclinic.exception_handler.exception.UnauthorizedException;
import com.bsdclinic.jwt.JWTUser;
import com.bsdclinic.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

import static com.bsdclinic.SecurityConfiguration.ENDPOINTS_WHITELIST;

@Component(SecurityBeanName.USER_JWT_FILTER_NAME)
@RequiredArgsConstructor
public class UserJwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Qualifier(SecurityBeanName.USER_SECURITY_SERVICE)
    private final AuthUserDetailService userDetailsService;

    private static String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request);
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            if (jwtService.validateJwtToken(accessToken)) {
                JWTUser jwtUser = jwtService.getPrincipalFromJwtToken(accessToken);
                UserPrincipal userDetails = userDetailsService.loadUserByUsername(jwtUser.getUsername());

                if (!Objects.equals(jwtUser.getTokenVersion(), userDetails.getTokenVersion())) {
                    throw new UnauthorizedException("Unauthorized");
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException ex) {
            customAuthenticationEntryPoint.commence(request, response, new UnauthorizedException(ex.getMessage()));
        }
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Stream.of(ENDPOINTS_WHITELIST).anyMatch(x -> {
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(x);
            return matcher.matches(request);
        });
    }
}
