package com.bsdclinic.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MedicineResponse {
    private String medicineId;
    private String title;
    private BigDecimal unitPrice;
    private String unit;
    private String dosage;
    private String sideEffect;
}
