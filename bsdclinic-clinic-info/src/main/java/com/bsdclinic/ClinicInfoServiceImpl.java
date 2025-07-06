package com.bsdclinic;

import com.bsdclinic.clinic_info.ClinicInfo;
import com.bsdclinic.constant.CacheKey;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicInfoServiceImpl implements ClinicInfoService {
    private final ClinicInfoRepository clinicInfoRepository;
    private final ClinicInfoMapper clinicInfoMapper;
    private final MessageProvider messageProvider;

    @Override
    @Cacheable(value = CacheKey.CLINIC_INFO)
    public ClinicInfoDto getClinicInfo() {
        List<ClinicInfo> clinicInfo = clinicInfoRepository.findAll();
        if (clinicInfo.isEmpty()) {
            throw new NotFoundException(messageProvider.getMessage("message.clinic_info.not_existed"));
        }

        ClinicInfoDto clinicInfoDto =  clinicInfoMapper.toDto(clinicInfo.getFirst());
        clinicInfoDto.setWorkingHoursJson(clinicInfoDto.convertWorkingHours(clinicInfoDto.getWorkingHours()));

        return clinicInfoDto;
    }

    @Override
    @CacheEvict(value = CacheKey.CLINIC_INFO, allEntries = true)
    public void updateClinicInfo(String clinicInfoId, ClinicInfoDto request) {
        if (Boolean.FALSE.equals(clinicInfoRepository.existsByClinicInfoId(clinicInfoId))) {
            throw new NotFoundException(messageProvider.getMessage("validation.no_exist.clinic_id"));
        }

        ClinicInfo clinicInfo = clinicInfoMapper.toEntity(request);
        clinicInfo.setClinicInfoId(clinicInfoId);

        clinicInfoRepository.save(clinicInfo);
    }
}
