package com.bsdclinic.invoice;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
public class PurchasedMedicine {
    private String title;
    private Integer purchasedQuantity;
    private BigDecimal unitPrice;
    private BigDecimal purchasedTotalPrice;
}
