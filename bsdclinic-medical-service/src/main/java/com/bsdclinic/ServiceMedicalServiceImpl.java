package com.bsdclinic;

import com.bsdclinic.category.CategoryAssignment;
import com.bsdclinic.dto.request.MedicalServiceRequest;
import com.bsdclinic.dto.request.MedicalServiceFilter;
import com.bsdclinic.dto.response.CategoryResponse;
import com.bsdclinic.dto.response.IMedicalServiceResponse;
import com.bsdclinic.dto.response.MedicalServiceResponse;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.medical_service.MedicalService;
import com.bsdclinic.medicine.Medicine_;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.repository.MedicalServiceRepository;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.specification.EntitySpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ServiceMedicalServiceImpl implements ServiceMedicalService {
    private final MedicalServiceRepository medicalServiceRepository;
    private final MedicalServiceMapper medicalServiceMapper;
    private final MessageProvider messageProvider;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public void create(MedicalServiceRequest request) {
        MedicalService service = medicalServiceMapper.toEntity(request);
        service = medicalServiceRepository.save(service);

        categoryService.createCategoryAssignments(service.getMedicalServiceId(), service.getTitle(), request.getCategoryIds());
    }

    @Override
    public DatatableResponse getMedicalServices(MedicalServiceFilter medicalServiceFilter) {
        Pageable pageable = PageRequest.of(
                medicalServiceFilter.getStart() / medicalServiceFilter.getLength(),
                medicalServiceFilter.getLength()
        );

        Specification<MedicalService> medicalServiceSpecification = EntitySpecifications.withFilter(
                medicalServiceFilter,
                Medicine_.MEDICINE_ID,
                Medicine_.TITLE,
                CategoryAssignment.class
        );

        Page<MedicalService> medicalServices = medicalServiceRepository.findAll(medicalServiceSpecification, pageable);

        List<String> serviceIds = medicalServices.stream().map(MedicalService::getMedicalServiceId).toList();
        Map<String, List<CategoryResponse>> serviceCategoryMap = categoryService.getAssignmentsByEntityIds(serviceIds);

        List<MedicalServiceResponse> serviceResponses = medicalServices.stream()
                .map(item -> {
                    MedicalServiceResponse response = medicalServiceMapper.toDto(item);
                    response.setServiceCategories(serviceCategoryMap.get(item.getMedicalServiceId()));
                    return response;
                })
                .toList();

        DatatableResponse<MedicalServiceResponse> datatableResponse = new DatatableResponse<>();
        datatableResponse.setData(serviceResponses);
        datatableResponse.setDraw(medicalServiceFilter.getDraw());
        Long totalRecord = medicalServices.getTotalElements();
        datatableResponse.setRecordsFiltered(totalRecord);
        datatableResponse.setRecordsTotal(totalRecord);

        return datatableResponse;
    }

    private MedicalService findById(String medicalServiceId) {
        return medicalServiceRepository.findById(medicalServiceId).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.medical_service"))
        );
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

    @Override
    public void updateMedicalService(String medicalServiceId, MedicalServiceRequest request) {
        MedicalService medicalService = findById(medicalServiceId);
        medicalService = medicalServiceMapper.toEntity(request, medicalService);
        medicalServiceRepository.save(medicalService);
    }

    @Override
    public void deleteMedicalService(String medicalServiceId) {
        MedicalService medicalService = findById(medicalServiceId);
        medicalServiceRepository.delete(medicalService);
    }
}
