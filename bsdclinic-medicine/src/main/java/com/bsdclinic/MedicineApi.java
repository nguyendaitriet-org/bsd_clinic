package com.bsdclinic;

import com.bsdclinic.dto.request.MedicineRequest;
import com.bsdclinic.dto.request.MedicineFilter;
import com.bsdclinic.dto.response.MedicineResponse;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MedicineApi {
    private final MedicineService medicineService;

    @RoleAuthorization.AdminAuthorization
    @PostMapping(WebUrl.API_ADMIN_MEDICINE)
    public void createMedicine(@RequestBody @Valid MedicineRequest request) {
        medicineService.createMedicine(request);
    }

    @RoleAuthorization.AdminAuthorization
    @PostMapping(WebUrl.API_ADMIN_MEDICINE_LIST)
    public DatatableResponse getMedicine(@RequestBody MedicineFilter request) {
        return medicineService.getMedicines(request);
    }

    @RoleAuthorization.AdminAuthorization
    @GetMapping(WebUrl.API_ADMIN_MEDICINE)
    public List<MedicineResponse> getMedicinesForSelection(@RequestParam String keyword) {
        return medicineService.getMedicinesForSelection(keyword);
    }

    @RoleAuthorization.AdminAuthorization
    @PutMapping(WebUrl.API_ADMIN_MEDICINE_WITH_ID)
    public void updateMedicine(
            @PathVariable String medicineId,
            @RequestBody @Valid MedicineRequest request
    ) {
        medicineService.updateMedicine(medicineId, request);
    }
}
