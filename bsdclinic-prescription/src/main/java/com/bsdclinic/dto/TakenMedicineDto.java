package com.bsdclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TakenMedicineDto {
    private String medicineId;
    private String title;
    private Integer purchasedQuantity;
    private BigDecimal unitPrice;
    private BigDecimal purchasedTotalPrice;
    private String usage;

    public TakenMedicineDto(
            String title,
            Integer purchasedQuantity,
            BigDecimal unitPrice,
            BigDecimal purchasedTotalPrice,
            String usage
    ) {
        this.title = title;
        this.purchasedQuantity = purchasedQuantity;
        this.unitPrice = unitPrice;
        this.purchasedTotalPrice = purchasedTotalPrice;
        this.usage = usage;
    }
}
