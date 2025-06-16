package com.bsdclinic;

import com.bsdclinic.dto.request.CreateServiceMedicalRequest;
import com.bsdclinic.medical_service.MedicalService;
import com.bsdclinic.repository.MedicalServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ServiceMedicalServiceImpl implements ServiceMedicalService {
    private final MedicalServiceRepository medicalServiceRepository;
    private final ServiceMedicalMapper serviceMedicalMapper;

    public void create(CreateServiceMedicalRequest request) {
        MedicalService service = serviceMedicalMapper.toEntity(request);

        medicalServiceRepository.save(service);
    }
}
