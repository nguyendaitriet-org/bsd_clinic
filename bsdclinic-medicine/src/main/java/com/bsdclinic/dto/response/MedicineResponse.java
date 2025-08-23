package com.bsdclinic.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MedicineResponse {
    private String medicineId;
    private String title;
    private BigDecimal unitPrice;
    private String unit;
    private String dosage;
    private String sideEffect;
    private List<CategoryResponse> medicineCategories;
}
