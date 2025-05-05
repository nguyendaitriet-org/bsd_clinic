package com.bsdclinic;

import com.bsdclinic.dto.request.CreateUserRequest;
import com.bsdclinic.dto.request.UserFilter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserApi {
    private final UserService userService;

    @PostMapping("list")
    @RoleAuthorization.AdminAuthorization
    public ResponseEntity getUsersByFilter(
            @RequestBody UserFilter userFilter,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        userFilter.setCurrentUserId(principal.getUserId());
        return ResponseEntity.ok(userService.getUserByFilter(userFilter));
    }

    @PostMapping
    @RoleAuthorization.AdminAuthorization
    public ResponseEntity createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        userService.createUser(createUserRequest);
        return ResponseEntity.ok().build();
    }
}
