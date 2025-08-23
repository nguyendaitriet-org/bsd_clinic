package com.bsdclinic;

import com.bsdclinic.category.CategoryAssignment;
import com.bsdclinic.dto.request.CategoryAssignmentRequest;
import com.bsdclinic.dto.request.MedicineRequest;
import com.bsdclinic.dto.request.MedicineFilter;
import com.bsdclinic.dto.response.CategoryResponse;
import com.bsdclinic.dto.response.MedicineResponse;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.medicine.Medicine;
import com.bsdclinic.medicine.Medicine_;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.specification.EntitySpecifications;
import io.jsonwebtoken.lang.Collections;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;
    private final MessageProvider messageProvider;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public void createMedicine(MedicineRequest request) {
        Medicine medicine = medicineMapper.toEntity(request);
        medicine = medicineRepository.save(medicine);

        saveMedicineCategoryAssignments(medicine.getMedicineId(), medicine.getTitle(), request.getCategoryIds());
    }

    private void saveMedicineCategoryAssignments(String medicineId, String medicineTitle, Set<String> categoryIds) {
        List<CategoryAssignmentRequest> assignmentRequests = categoryIds.stream()
                .map(categoryId -> CategoryAssignmentRequest.builder()
                        .entityId(medicineId)
                        .entityTitle(medicineTitle)
                        .categoryId(categoryId)
                        .build()
                ).toList();
        categoryService.createCategoryAssignments(assignmentRequests);
    }

    @Override
    public DatatableResponse getMedicines(MedicineFilter medicineFilter) {
        Pageable pageable = PageRequest.of(
                medicineFilter.getStart() / medicineFilter.getLength(),
                medicineFilter.getLength()
        );

        Specification<Medicine> medicineSpecification = EntitySpecifications.withFilter(
                medicineFilter,
                Medicine_.MEDICINE_ID,
                Medicine_.TITLE,
                CategoryAssignment.class
        );

        Page<Medicine> medicines = medicineRepository.findAll(medicineSpecification, pageable);

        List<String> medicineIds = medicines.stream().map(Medicine::getMedicineId).toList();
        Map<String, List<CategoryResponse>> medicineCategoryMap = categoryService.getAssignmentsByEntityIds(medicineIds);

        List<MedicineResponse> medicineResponses = medicines.stream()
                .map(item -> {
                    MedicineResponse response = medicineMapper.toDto(item);
                    response.setMedicineCategories(medicineCategoryMap.get(item.getMedicineId()));
                    return response;
                })
                .toList();

        DatatableResponse<MedicineResponse> datatableResponse = new DatatableResponse<>();
        datatableResponse.setData(medicineResponses);
        datatableResponse.setDraw(medicineFilter.getDraw());
        Long totalRecord = medicines.getTotalElements();
        datatableResponse.setRecordsFiltered(totalRecord);
        datatableResponse.setRecordsTotal(totalRecord);

        return datatableResponse;
    }

    @Override
    public List<MedicineResponse> getMedicinesForSelection(String keyword) {
        List<Medicine> medicalServices = medicineRepository.findAllByKeyword(keyword);
        return medicineMapper.toDtoList(medicalServices);
    }

    @Override
    @Transactional
    public void updateMedicine(String medicineId, MedicineRequest request) {
        Medicine medicine = getMedicine(medicineId);
        medicine = medicineMapper.toEntity(medicine, request);
        medicineRepository.save(medicine);
        categoryService.deleteAssignmentByEntityId(medicineId);
        Set<String> categoryIds = request.getCategoryIds();
        if (!Collections.isEmpty(categoryIds)) {
            saveMedicineCategoryAssignments(medicine.getMedicineId(), medicine.getTitle(), categoryIds);
        }
    }

    @Override
    @Transactional
    public void deleteMedicine(String medicineId) {
        Medicine medicine = getMedicine(medicineId);
        medicineRepository.delete(medicine);
        categoryService.deleteAssignmentByEntityId(medicineId);
    }

    private Medicine getMedicine(String medicineId) {
        return medicineRepository.findById(medicineId).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.medicine"))
        );
    }
}
