package com.bsdclinic.prescription;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ExternalMedicine {
    private String title;
    private String purchasedQuantity;
    private String usage;
}
