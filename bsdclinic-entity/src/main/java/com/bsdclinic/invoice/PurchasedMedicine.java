package com.bsdclinic.invoice;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PurchasedMedicine {
    private String title;
    private Integer purchasedQuantity;
    private BigDecimal unitPrice;
    private BigDecimal purchasedTotalPrice;
}
