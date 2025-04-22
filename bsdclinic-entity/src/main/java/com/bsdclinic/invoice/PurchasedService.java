package com.bsdclinic.invoice;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PurchasedService {
    private String title;
    private BigDecimal unitPrice;
}
