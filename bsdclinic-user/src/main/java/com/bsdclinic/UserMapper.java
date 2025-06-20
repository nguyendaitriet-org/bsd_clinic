package com.bsdclinic;

import com.bsdclinic.dto.request.CreateUserRequest;
import com.bsdclinic.dto.request.UserInfoRequest;
import com.bsdclinic.dto.response.UserResponse;
import com.bsdclinic.mapper.DateMapper;
import com.bsdclinic.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {DateMapper.class})
public interface UserMapper {
    User toEntity(CreateUserRequest request);

    UserResponse toDto(User user);

    User toEntity(UserInfoRequest request, @MappingTarget User user);
}
