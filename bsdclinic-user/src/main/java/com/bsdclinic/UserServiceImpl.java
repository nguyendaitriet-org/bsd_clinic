package com.bsdclinic;

import com.bsdclinic.dto.request.CreateUserRequest;
import com.bsdclinic.dto.request.UserFilter;
import com.bsdclinic.dto.response.IUserResponse;
import com.bsdclinic.dto.response.UserResponse;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.repository.RoleRepository;
import com.bsdclinic.repository.UserRepository;
import com.bsdclinic.repository.UserSpecifications;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.user.Role;
import com.bsdclinic.user.User;
import com.bsdclinic.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final MessageProvider messageProvider;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
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
                Sort.by(Sort.Direction.DESC, "createdAt")
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
        String message = messageProvider.getMessage("validation.no_exist.user_id");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(message + userId));
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }
}
