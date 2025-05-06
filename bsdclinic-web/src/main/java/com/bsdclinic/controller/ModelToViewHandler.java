package com.bsdclinic.controller;

import com.bsdclinic.UserPrincipal;
import com.bsdclinic.UserService;
import com.bsdclinic.dto.response.IUserResponse;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.user.RoleConstant;
import com.bsdclinic.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

/* This class adds specified models to all views by using @ControllerAdvice and @ModelAttribute */
@ControllerAdvice
@RequiredArgsConstructor
public class ModelToViewHandler {
    private final UserService userService;
    private final MessageProvider messageProvider;

    /* This method pass 'user' object to all returned views in this controller */
    @ModelAttribute("user")
    public IUserResponse getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        return principal != null ? userService.getUserById(principal.getUserId()) : null;
    }

    @ModelAttribute("roleTitleMap")
    public Map<String, String> getRoleTitles() {
        return messageProvider.getMessageMap("role", RoleConstant.getAllNames());
    }

    @ModelAttribute("userStatusMap")
    public Map<String, String> getUserStatus() {
        return messageProvider.getMessageMap("user.status", UserStatus.getAllNames());
    }
}
