package com.bsdclinic;

import com.bsdclinic.dto.request.CreateUserRequest;
import com.bsdclinic.dto.response.IUserResponse;
import com.bsdclinic.user.Role;

import java.util.List;

public interface UserService {
    List<Role> getAllRoles();
    void createUser(CreateUserRequest request);
    IUserResponse getUserById(String userId);
    void changePassword(String userId, String newPassword);
}
