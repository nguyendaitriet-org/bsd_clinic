package com.bsdclinic;

import com.bsdclinic.dto.request.CreateUserRequest;

public interface UserService {
    void createUser(CreateUserRequest request);
}
