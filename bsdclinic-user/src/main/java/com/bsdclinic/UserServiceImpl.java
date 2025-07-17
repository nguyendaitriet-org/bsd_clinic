package com.bsdclinic;

import com.bsdclinic.constant.CacheKey;
import com.bsdclinic.constant.ComponentName;
import com.bsdclinic.dto.request.CreateUserRequest;
import com.bsdclinic.dto.request.UpdateUserByAdminRequest;
import com.bsdclinic.dto.request.UserFilter;
import com.bsdclinic.dto.request.UserInfoRequest;
import com.bsdclinic.dto.response.AvatarResponse;
import com.bsdclinic.dto.response.IUserResponse;
import com.bsdclinic.dto.response.IUserSelectResponse;
import com.bsdclinic.dto.response.UserResponse;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.repository.RoleRepository;
import com.bsdclinic.repository.UserRepository;
import com.bsdclinic.repository.UserSpecifications;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.storage.FileStorageService;
import com.bsdclinic.storage.LocalFileStorageService;
import com.bsdclinic.user.Role;
import com.bsdclinic.user.User;
import com.bsdclinic.user.UserStatus;
import com.bsdclinic.user.User_;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final MessageProvider messageProvider;
    private final LocalFileStorageService localFileStorageService;

    private static final String AVATAR_FOLDER = "avatar";

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    @CacheEvict(value = CacheKey.USERS_FOR_SELECT, allEntries = true)
    public void createUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        user.setStatus(UserStatus.ACTIVE.name());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    @Override
    public DatatableResponse getUserByFilter(UserFilter userFilter) {
        Specification<User> userSpecification = UserSpecifications.withFilter(userFilter);
        Pageable pageable = PageRequest.of(
                userFilter.getStart() / userFilter.getLength(),
                userFilter.getLength(),
                Sort.by(Sort.Direction.DESC, User_.CREATED_AT)
        );

        Page<User> users = userRepository.findAll(userSpecification, pageable);

        List<UserResponse> userResponse = users.stream()
                .map(userMapper::toDto)
                .toList();

        DatatableResponse<UserResponse> datatableResponse = new DatatableResponse<>();
        datatableResponse.setData(userResponse);
        datatableResponse.setDraw(userFilter.getDraw());
        Long totalRecord = users.getTotalElements();
        datatableResponse.setRecordsFiltered(totalRecord);
        datatableResponse.setRecordsTotal(totalRecord);

        return datatableResponse;
    }

    @Override
    public IUserResponse getUserById(String userId) {
        return userRepository.findByIdRole(userId);
    }

    @Override
    public void changePassword(String userId, String newPassword) {
        User user = findById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    @Override
    @CacheEvict(value = CacheKey.USERS_FOR_SELECT, allEntries = true)
    public void updateByAdmin(UpdateUserByAdminRequest request) {
        User user = findById(request.getUserId());
        user.setRoleId(request.getRoleId());
        user.setStatus(request.getStatus());

        userRepository.save(user);
    }

    @Override
    public AvatarResponse saveAvatar(MultipartFile avatar, String userId) {
        String fileName = userId + "." + FilenameUtils.getExtension(avatar.getOriginalFilename());
        localFileStorageService.deleteFilesByBaseName(userId, AVATAR_FOLDER);
        localFileStorageService.uploadFile(avatar, AVATAR_FOLDER, fileName);

        return new AvatarResponse(fileName);
    }

    @Override
    public Resource getAvatar(String userId) {
        Resource avatarResource = localFileStorageService.downloadFileByBaseName(userId, AVATAR_FOLDER);
        if (avatarResource == null) {
            return localFileStorageService.downloadFile("default.png", AVATAR_FOLDER);
        }
        return avatarResource;
    }

    private User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        messageProvider.getMessage("validation.no_exist.user_id") + " " + userId));
    }

    @Override
    public void updateUserInfo(String userId, UserInfoRequest userInfoRequest){
        User user = findById(userId);
        user = userMapper.toEntity(userInfoRequest, user);
        userRepository.save(user);
    }

    @Override
    @Cacheable(value = CacheKey.USERS_FOR_SELECT)
    public List<IUserSelectResponse> getUsersForSelectByRoles(List<String> roleCodes) {
        return userRepository.findUsersForSelectByRoles(roleCodes);
    }
}
