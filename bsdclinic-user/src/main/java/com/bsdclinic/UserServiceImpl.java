package com.bsdclinic;

import com.bsdclinic.dto.request.CreateUserRequest;
import com.bsdclinic.dto.response.IUserResponse;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.user.Role;
import com.bsdclinic.user.User;
import com.bsdclinic.user.UserStatus;
import lombok.RequiredArgsConstructor;
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
    public IUserResponse getUserById(String userId) {
        return userRepository.findByIdRole(userId);
    }
    @Override
    public void changePassword(String userId, String newPassword) {
        String message = messageProvider.getMessage("{validation.no_exist.user_id}");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(message + userId));
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }
}
