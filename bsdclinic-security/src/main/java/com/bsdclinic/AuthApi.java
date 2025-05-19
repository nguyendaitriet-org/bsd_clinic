package com.bsdclinic;

import com.bsdclinic.dto.LoginParam;
import com.bsdclinic.exception_handler.exception.UnauthorizedException;
import com.bsdclinic.jwt.JwtService;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthApi {
    @NotNull
    @Qualifier(SecurityBeanName.USER_DAO_AUTHENTICATION_PROVIDER)
    private final DaoAuthenticationProvider userAuthenticationProvider;

    private final JwtService jwtService;

    private final MessageProvider messageProvider;

    @PostMapping("/api/admin/login")
    public ResponseEntity uniLogin(@Valid @RequestBody LoginParam loginParam) {
        Authentication authentication;
        try {
            authentication = userAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(loginParam.getEmail(),
                            loginParam.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException(messageProvider.getMessage("message.user.not_existed_or_blocked"));
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(principal.getUserId(), principal.getUsername());
        ResponseCookie cookie = ResponseCookie.from("JWT", accessToken)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .maxAge(SecurityConfiguration.TOKEN_EXPIRATION_TIME)
                .build();

        Map<String, String> extraData = new HashMap<>();
        extraData.put("redirectUrl", WebUrl.ADMIN_HOME);
        String message = messageProvider.getMessage("message.login.success");
        extraData.put("message", message);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(extraData);
    }
}
