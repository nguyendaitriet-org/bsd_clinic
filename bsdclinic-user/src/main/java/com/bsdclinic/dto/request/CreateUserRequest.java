package com.bsdclinic.dto.request;

import com.bsdclinic.validation.RuleAnnotation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    @RuleAnnotation.ExistedEmail( message = "{message.login.invalid_email}")
    @NotBlank(message = "{validation.required.email}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    @Pattern(regexp = "[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$", message = "{message.login.format_email}")
    private String email;

    @NotBlank(message = "{validation.required.full_name}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String fullName;

    @NotBlank(message = "{validation.required.phone}")
    @Size(max = 20, message = "{validation.input.max_length.20}")
    private String phone;

    @NotBlank(message = "{validation.required.password}")
    @Size(min = 6, max = 16, message = "{validation.required.password_character}")
    private String password;

    @NotBlank(message = "{validation.required.password_confirmation}")
    private String passwordConfirmation;

    @NotBlank(message = "{validation.required.role}")
    private String roleId;
}
