package com.bsdclinic.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginParam {
    @NotBlank(message = "{validation.required.email}")
    private String email;

    @NotBlank(message="{validation.required.password}")
    private String password;
}


