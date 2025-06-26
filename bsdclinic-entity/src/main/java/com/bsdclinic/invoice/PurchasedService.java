package com.bsdclinic.invoice;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
public class PurchasedService {
    private String title;
    private BigDecimal price;
}
