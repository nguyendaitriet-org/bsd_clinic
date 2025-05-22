package com.bsdclinic;

import com.bsdclinic.dto.request.*;
import com.bsdclinic.dto.response.AvatarResponse;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.url.WebUrl;
import com.bsdclinic.validation.RuleAnnotation;
import com.bsdclinic.validation.ValidationSequence;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping(WebUrl.API_ADMIN_USER_ENDPOINT)
@Validated
public class UserApi {
    private final UserService userService;

    @RoleAuthorization.AdminAuthorization
    @PostMapping("/list")
    public DatatableResponse getUsersByFilter(
            @RequestBody UserFilter userFilter,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        userFilter.setCurrentUserId(principal.getUserId());
        return userService.getUserByFilter(userFilter);
    }

    @RoleAuthorization.AdminAuthorization
    @PostMapping
    public void createUser(@RequestBody @Valid CreateUserRequest request) {
        userService.createUser(request);
    }

    @RoleAuthorization.AdminAuthorization
    @PatchMapping
    public void updateUserByAdmin(@RequestBody @Validated(ValidationSequence.class) UpdateUserByAdminRequest request) {
        userService.updateByAdmin(request);
    }

    @RoleAuthorization.AuthenticatedUser
    @PutMapping("/change-password")
    public void changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        userService.changePassword(principal.getUserId(), changePasswordRequest.getNewPassword());
    }

    @RoleAuthorization.AuthenticatedUser
    @PostMapping("/avatar")
    public AvatarResponse saveAvatar(
            @RequestParam @RuleAnnotation.ValidAvatar MultipartFile avatar,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        return userService.saveAvatar(avatar, principal.getUserId());
    }

    @RoleAuthorization.AuthenticatedUser
    @GetMapping(value = "/avatar")
    public byte[] getAvatar(@AuthenticationPrincipal UserPrincipal principal) throws IOException {
        return userService.getAvatar(principal.getUserId()).getContentAsByteArray();
    }

    @RoleAuthorization.AuthenticatedUser
    @PatchMapping(value = "/profile")
    public void updateUserInfo(@AuthenticationPrincipal UserPrincipal principal, @RequestBody @Valid UserInfoRequest request) throws IOException {
        userService.updateUserInfo(principal.getUserId(),request);
    }
}
