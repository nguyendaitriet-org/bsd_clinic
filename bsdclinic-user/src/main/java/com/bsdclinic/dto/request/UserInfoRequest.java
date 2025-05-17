package com.bsdclinic.dto.request;

import com.bsdclinic.validation.RuleAnnotation;
import com.bsdclinic.validation.ValidationConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoRequest {
    @NotBlank(message = "{validation.required.email}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    @Pattern(regexp = ValidationConstant.EMAIL_PATTERN, message = "{validation.format.email}")
    @RuleAnnotation.UniqueEmailExceptCurrent( message = "{validation.existed.email}")
    private String email;

    @NotBlank(message = "{validation.required.full_name}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String fullName;

    @NotBlank(message = "{validation.required.phone}")
    @Size(max = 20, message = "{validation.input.max_length.20}")
    @RuleAnnotation.UniquePhoneExceptCurrent( message = "{validation.existed.phone}")
    private String phone;
}
