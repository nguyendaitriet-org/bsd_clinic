package com.bsdclinic;

import com.bsdclinic.auth_user.UserRepository;
import com.bsdclinic.dto.LoginParam;
import com.bsdclinic.jwt.JwtService;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.user.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    /*-------------[TEST]-------------*/
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    /*-------------------------------*/

    @PostMapping("/api/login")
    public ResponseEntity uniLogin(@Valid @RequestBody LoginParam loginParam) {
        Authentication authentication = userAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(loginParam.getEmail(),
                loginParam.getPassword())
        );
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(principal.getUserId(), principal.getUsername());
        ResponseCookie cookie = ResponseCookie.from("JWT", accessToken)
                .httpOnly(false)
                .secure(false)
                .path("/")
                .maxAge(SecurityConfiguration.TOKEN_EXPIRATION_TIME)
                .build();

        Map<String, String> extraData = new HashMap<>();
        extraData.put("redirectUrl", "/admin");
        extraData.put("message", messageProvider.getMessage("message.login.success"));

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(extraData);
    }

    /*-------------[TEST]-------------*/
    @PostMapping("/api/users/test-creation")
    public ResponseEntity testCreation() {
        User newUser = new User()
                .setEmail("admin@gmail.com")
                .setPassword(passwordEncoder.encode("72tAelf8zVxrg_1BvJw4o"))
                .setStatus("ACTIVE")
                .setRoleId("1QhKeQzl3Ti16qL-vCdws")
                .setFullName("Test User");
        userRepository.save(newUser);
        return ResponseEntity.ok(messageProvider.getMessage("message.create.success"));
    }
    /*-------------------------------*/

}
