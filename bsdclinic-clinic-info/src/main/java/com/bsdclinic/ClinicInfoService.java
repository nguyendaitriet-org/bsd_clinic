package com.bsdclinic;

public interface ClinicInfoService {
    ClinicInfoDto getClinicInfo();
    void updateClinicInfo(String clinicInfoId, ClinicInfoDto clinicInfo);
}
