package com.bsdclinic;

public interface ClinicInfoService {
    ClinicInfoDto getClinicInfo();
    void evictClinicInfoCache();
}
