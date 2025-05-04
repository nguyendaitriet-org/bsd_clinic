package com.bsdclinic.dto.request;

import com.bsdclinic.validation.GeneralRuleAnnotation;
import com.bsdclinic.validation.RuleAnnotation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@GeneralRuleAnnotation.FieldMatch(
        first = "password",
        second = "newPasswordConfirmation",
        errorMessage = "{validation.no_match.password_confirmation}"
)
public class ChangePasswordRequest {
    @NotBlank(message = "")
    @RuleAnnotation.CheckOldPassword(message = "")
    private String oldPassword;

    @NotBlank(message = "")
    @Size(min = 6, max = 16, message = "{validation.length.password}")
    private String newPassword;

    @NotBlank(message = "")
    private String newPasswordConfirmation;
}
