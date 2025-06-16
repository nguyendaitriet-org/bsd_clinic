package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicalServiceRequest;
import com.bsdclinic.dto.request.MedicalServiceFilter;
import com.bsdclinic.medical_service.MedicalService;
import com.bsdclinic.repository.MedicalServiceRepository;
import com.bsdclinic.response.DatatableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ServiceMedicalServiceImpl implements ServiceMedicalService {
    private final MedicalServiceRepository medicalServiceRepository;
    private final MedicalServiceMapper medicalServiceMapper;

    public void create(CreateMedicalServiceRequest request) {
        MedicalService service = medicalServiceMapper.toEntity(request);

        medicalServiceRepository.save(service);
    }

    @Override
    public DatatableResponse getMedicalServices(MedicalServiceFilter medicalServiceFilter) {

        return null;
    }
}
