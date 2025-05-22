package com.bsdclinic;

import com.bsdclinic.clinic_info.ClinicInfo;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicInfoServiceImpl implements ClinicInfoService {
    private final ClinicInfoRepository clinicInfoRepository;
    private final ClinicInfoMapper clinicInfoMapper;
    private final MessageProvider messageProvider;

    @Override
    public ClinicInfoDto getClinicInfo() {
        List<ClinicInfo> clinicInfo = clinicInfoRepository.findAll();
        if (clinicInfo.isEmpty()) {
            throw new NotFoundException(messageProvider.getMessage("message.clinic_info.not_existed"));
        }

        return clinicInfoMapper.toDto(clinicInfo.getFirst());
    }
}
