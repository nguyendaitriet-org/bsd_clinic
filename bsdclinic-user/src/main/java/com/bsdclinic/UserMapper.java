package com.bsdclinic;

import com.bsdclinic.dto.request.CreateUserRequest;
import com.bsdclinic.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
@Component
public interface UserMapper {
    User toEntity(CreateUserRequest request);
}
