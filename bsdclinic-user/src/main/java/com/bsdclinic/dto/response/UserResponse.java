package com.bsdclinic.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String userId;
    private String email;
    private String fullName;
    private String phone;
    private String createdAt;
    private String roleId;
    private String status;
}
