package com.bsdclinic;

import com.bsdclinic.dto.request.CreateUserRequest;
import com.bsdclinic.dto.request.UpdateUserByAdminRequest;
import com.bsdclinic.dto.request.UserFilter;
import com.bsdclinic.dto.request.UserInfoRequest;
import com.bsdclinic.dto.response.AvatarResponse;
import com.bsdclinic.dto.response.IUserResponse;
import com.bsdclinic.dto.response.IUserSelectResponse;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.user.Role;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<Role> getAllRoles();
    void createUser(CreateUserRequest request);
    DatatableResponse getUserByFilter(UserFilter userFilter);
    IUserResponse getUserById(String userId);
    void changePassword(String userId, String newPassword);
    void updateByAdmin(UpdateUserByAdminRequest request);
    AvatarResponse saveAvatar(MultipartFile avatar, String userId);
    Resource getAvatar(String userId);
    void updateUserInfo(String userId, UserInfoRequest userInfoRequest);
    List<IUserSelectResponse> getUsersForSelectByRoles(List<String> roleCodes);
}
