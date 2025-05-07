package com.bsdclinic;

import com.bsdclinic.dto.request.ChangePasswordRequest;
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

    @RoleAuthorization.AdminAuthorization
    @PostMapping("/list")
    public ResponseEntity getUsersByFilter(
            @RequestBody UserFilter userFilter,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        userFilter.setCurrentUserId(principal.getUserId());
        return ResponseEntity.ok(userService.getUserByFilter(userFilter));
    }

    @RoleAuthorization.AdminAuthorization
    @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        userService.createUser(createUserRequest);
        return ResponseEntity.ok().build();
    }

    @RoleAuthorization.AuthenticatedUser
    @PutMapping("/change-password")
    public void changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        userService.changePassword(principal.getUserId(), changePasswordRequest.getNewPassword());
    }
}
