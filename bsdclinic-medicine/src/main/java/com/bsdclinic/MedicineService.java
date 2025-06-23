package com.bsdclinic;

import com.bsdclinic.dto.request.MedicineRequest;
import com.bsdclinic.dto.request.MedicineFilter;
import com.bsdclinic.dto.response.MedicineResponse;
import com.bsdclinic.response.DatatableResponse;

import java.util.List;

public interface MedicineService {
    void createMedicine(MedicineRequest request);
    DatatableResponse getMedicines(MedicineFilter medicineFilter);
    List<MedicineResponse> getMedicinesForSelection(String keyword);
    void updateMedicine(String medicineId, MedicineRequest request);
    void deleteMedicine(String medicineId);
}
