package com.bsdclinic;

import com.bsdclinic.dto.request.CreateUserRequest;
import com.bsdclinic.dto.response.UserResponse;
import com.bsdclinic.mapper.DateMapper;
import com.bsdclinic.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {DateMapper.class})
public interface UserMapper {
    User toEntity(CreateUserRequest request);

    UserResponse toDto(User user);
}
