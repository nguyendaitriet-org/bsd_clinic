package com.bsdclinic.dto;

public interface IUserResult {
    String getUserId();
    String getEmail();
    String getPassword();
    String getStatus();
    Integer getTokenVersion();
    String getRole();
}
