package com.bsdclinic.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String fullName;
    private String email;
    private String phone;
    private String status;
    private String role;
}
