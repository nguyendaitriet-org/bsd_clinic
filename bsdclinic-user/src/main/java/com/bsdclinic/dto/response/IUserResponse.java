package com.bsdclinic.dto.response;

import java.time.Instant;

public interface IUserResponse {
    String getEmail();
    String getPhone();
    String getFullName();
    String getStatus();
    String getRole();
    Instant getCreatedAt();
    String getUserId();
}
