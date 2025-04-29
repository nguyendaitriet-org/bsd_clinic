package com.bsdclinic.controller;

import com.bsdclinic.UserPrincipal;
import com.bsdclinic.UserService;
import com.bsdclinic.dto.response.IUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/* This class adds specified models to all views by using @ControllerAdvice and @ModelAttribute */
@ControllerAdvice
@RequiredArgsConstructor
public class ModelToViewHandler {
    private final UserService userService;

    /* This method pass 'user' object to all returned views in this controller */
    @ModelAttribute("user")
    public IUserResponse getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        return principal != null ? userService.getUserById(principal.getUserId()) : null;
    }
}
