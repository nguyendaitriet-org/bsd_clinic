package com.bsdclinic;

import com.bsdclinic.dto.request.CreateUserRequest;
import com.bsdclinic.user.Role;

import java.util.List;

public interface UserService {
    void createUser(CreateUserRequest request);
    List<Role> getAllRoles();
}
