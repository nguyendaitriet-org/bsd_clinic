package com.bsdclinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginParam {
    @NotBlank(message = "{validation.required.email")
    @Size(max = 255, message = "{validation.max_length.email}")
    private String email;

    @NotBlank(message="{validation.required.password")
    @Size(max = 255, message = "{validation.max_length.password}")
    private String password;
}


