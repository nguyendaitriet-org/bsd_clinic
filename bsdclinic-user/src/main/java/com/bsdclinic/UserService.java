package com.bsdclinic;

import com.bsdclinic.dto.request.CreateUserRequest;
import com.bsdclinic.dto.request.UserFilter;
import com.bsdclinic.dto.response.IUserResponse;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.user.Role;

import java.util.List;

public interface UserService {
    List<Role> getAllRoles();
    void createUser(CreateUserRequest request);
    DatatableResponse getUserByFilter(UserFilter userFilter);
    IUserResponse getUserById(String userId);
}
