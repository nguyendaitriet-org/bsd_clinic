package com.bsdclinic.auth_user;

import com.bsdclinic.SecurityBeanName;
import com.bsdclinic.UserPrincipal;
import com.bsdclinic.dto.IUserResult;
import com.bsdclinic.exception_handler.exception.ForbiddenException;
import com.bsdclinic.exception_handler.exception.UnauthorizedException;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service(SecurityBeanName.USER_SECURITY_SERVICE)
@RequiredArgsConstructor
public class AuthUserDetailService implements UserDetailsService {
    private final UserSecurityRepository userSecurityRepository;
    private final MessageProvider messageProvider;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        IUserResult user = userSecurityRepository.findByEmailWithRole(email);

        if (user == null) {
            throw new UnauthorizedException(messageProvider.getMessage("validation.no_exist.email"));
        }

        if (user.getStatus().equals(UserStatus.BLOCKED.name())) {
            throw new ForbiddenException(messageProvider.getMessage("error.403"));
        }

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole())
        );

        return new UserPrincipal(user.getUserId(), user.getEmail(), user.getPassword(), authorities);
    }
}
