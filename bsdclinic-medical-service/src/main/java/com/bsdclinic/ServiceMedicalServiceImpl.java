package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicalServiceRequest;
import com.bsdclinic.dto.request.MedicalServiceFilter;
import com.bsdclinic.dto.response.IMedicalServiceResponse;
import com.bsdclinic.dto.response.MedicalServiceResponse;
import com.bsdclinic.medical_service.MedicalService;
import com.bsdclinic.repository.MedicalServiceRepository;
import com.bsdclinic.response.DatatableResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Pageable pageable = PageRequest.of(
                medicalServiceFilter.getStart() / medicalServiceFilter.getLength(),
                medicalServiceFilter.getLength()
        );

        Page<MedicalService> medicalServices;
        String keyword = medicalServiceFilter.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            medicalServices = medicalServiceRepository.findAllByKeywordWithPage(keyword, pageable);
        } else {
            medicalServices = medicalServiceRepository.findAll(pageable);
        }
        List<MedicalServiceResponse> medicalServiceResponse = medicalServices.stream()
                .map(medicalServiceMapper::toDto)
                .toList();

        DatatableResponse<MedicalServiceResponse> datatableResponse = new DatatableResponse<>();
        datatableResponse.setData(medicalServiceResponse);
        datatableResponse.setDraw(medicalServiceFilter.getDraw());
        Long totalRecord = medicalServices.getTotalElements();
        datatableResponse.setRecordsFiltered(totalRecord);
        datatableResponse.setRecordsTotal(totalRecord);

        return datatableResponse;
    }

    @Override
    public List<MedicalServiceResponse> getMedicalServicesForSelection(String keyword) {
        List<MedicalService> medicalServices = medicalServiceRepository.findAllByKeyword(keyword);
        return medicalServiceMapper.toDtoList(medicalServices);
    }

    @Override
    public List<IMedicalServiceResponse> getMedicalServicesByMedicalRecordId(String medicalRecordId) {
        return medicalServiceRepository.getMedicalServicesByMedicalRecordId(medicalRecordId);
    }
}
