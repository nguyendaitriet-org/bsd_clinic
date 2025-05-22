package com.bsdclinic;

import com.bsdclinic.clinic_info.ClinicInfo;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicInfoServiceImpl implements ClinicInfoService {
    private final ClinicInfoRepository clinicInfoRepository;
    private final ClinicInfoMapper clinicInfoMapper;

    @Override
    public ClinicInfoDto getClinicInfo() {
        List<ClinicInfo> clinicInfo = clinicInfoRepository.findAll();
        if (clinicInfo.isEmpty()) {
            throw new NotFoundException("");
        }

        return clinicInfoMapper.toDto(clinicInfo.getFirst());
    }
}
