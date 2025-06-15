package com.bsdclinic;

import com.bsdclinic.dto.request.CreateServiceMedicalRequest;
import com.bsdclinic.user.Role;
import java.util.List;

public interface ServiceMedicalService {
    void create(CreateServiceMedicalRequest request);

}
