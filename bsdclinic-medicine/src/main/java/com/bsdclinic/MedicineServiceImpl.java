package com.bsdclinic;

import com.bsdclinic.dto.request.MedicineRequest;
import com.bsdclinic.dto.request.MedicineFilter;
import com.bsdclinic.dto.response.MedicineResponse;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.medicine.Medicine;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.response.DatatableResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;
    private final MessageProvider messageProvider;

    @Override
    public void createMedicine(MedicineRequest request) {
        Medicine medicine = medicineMapper.toEntity(request);
        medicineRepository.save(medicine);
    }

    @Override
    public DatatableResponse getMedicines(MedicineFilter medicineFilter) {
        Pageable pageable = PageRequest.of(
                medicineFilter.getStart() / medicineFilter.getLength(),
                medicineFilter.getLength()
        );

        Page<Medicine> medicines;
        String keyword = medicineFilter.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            medicines = medicineRepository.findAllByKeywordWithPage(keyword, pageable);
        } else {
            medicines = medicineRepository.findAll(pageable);
        }
        List<MedicineResponse> medicineResponses = medicines.stream()
                .map(medicineMapper::toDto)
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
    public void updateMedicine(String medicineId, MedicineRequest request) {
        Medicine medicine = medicineRepository.findById(medicineId).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.medicine"))
        );
        medicine = medicineMapper.toEntity(medicine, request);
        medicineRepository.save(medicine);
    }

    @Override
    public void deleteMedicine(String medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.medicine"))
        );
        medicineRepository.delete(medicine);
    }
}
