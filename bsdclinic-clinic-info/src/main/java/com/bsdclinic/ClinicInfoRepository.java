package com.bsdclinic;

import com.bsdclinic.clinic_info.ClinicInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicInfoRepository extends JpaRepository<ClinicInfo, String> {
}
