package com.bsdclinic.dto.request;

import com.bsdclinic.validation.GeneralRuleAnnotation;
import com.bsdclinic.validation.RuleAnnotation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@GeneralRuleAnnotation.FieldMatch(
        first = "newPassword",
        second = "newPasswordConfirmation",
        errorMessage = "{validation.no_match.password_confirmation}"
)
public class ChangePasswordRequest {
    @RuleAnnotation.CheckOldPassword
    private String oldPassword;

    @NotBlank(message = "validation.required.new_password")
    @Size(min = 6, max = 16, message = "{validation.length.password}")
    private String newPassword;

    @NotBlank(message = "{validation.required.password_confirmation}")
    private String newPasswordConfirmation;
}
