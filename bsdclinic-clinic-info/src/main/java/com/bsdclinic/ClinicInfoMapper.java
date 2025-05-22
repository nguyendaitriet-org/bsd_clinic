package com.bsdclinic;

import com.bsdclinic.clinic_info.ClinicInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClinicInfoMapper {
    ClinicInfoDto toDto(ClinicInfo clinicInfo);
}
