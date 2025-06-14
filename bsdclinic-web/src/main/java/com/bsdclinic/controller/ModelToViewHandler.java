package com.bsdclinic.controller;

import com.bsdclinic.UserPrincipal;
import com.bsdclinic.UserService;
import com.bsdclinic.appointment.ActionStatus;
import com.bsdclinic.clinic_info.DayOfWeek;
import com.bsdclinic.dto.response.IUserResponse;
import com.bsdclinic.url.WebUrl;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.user.Gender;
import com.bsdclinic.user.RoleConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.HandlerMapping;

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

    @ModelAttribute("genderMap")
    public Map<String, String> getGender() {
        return messageProvider.getMessageMap("gender", Gender.getAllNames());
    }

    @ModelAttribute("dayOfWeekMap")
    public Map<String, String> getDayOfWeek() {
        return messageProvider.getMessageMap("day", DayOfWeek.getAllNames());
    }

    @ModelAttribute("webUrl")
    public WebUrl getWebUrl() {
        return new WebUrl();
    }

    @ModelAttribute
    public void addAminAttributes(Map<String, Object> model, HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        if (path != null && path.startsWith(WebUrl.ADMIN_HOME)) {
            model.put("roleTitleMap", getRoleTitles());
            model.put("appointmentStatusMap", getAppointmentStatus());
        }
    }

    public Map<String, String> getRoleTitles() {
        return messageProvider.getMessageMap("role", RoleConstant.getAllNames());
    }

    public Map<String, String> getAppointmentStatus() {
        return messageProvider.getMessageMap("appointment.action_status", ActionStatus.getAllNames());
    }

}
