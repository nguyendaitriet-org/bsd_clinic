package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicineRequest;
import com.bsdclinic.dto.request.MedicineFilter;
import com.bsdclinic.dto.response.MedicineResponse;
import com.bsdclinic.response.DatatableResponse;

import java.util.List;

public interface MedicineService {
    void createMedicine(CreateMedicineRequest request);
    DatatableResponse getMedicines(MedicineFilter medicineFilter);
    List<MedicineResponse> getMedicinesForSelection(String keyword);
}
