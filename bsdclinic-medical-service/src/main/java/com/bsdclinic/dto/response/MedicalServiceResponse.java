package com.bsdclinic.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MedicalServiceResponse {
    private String title;
    private BigDecimal price;
    private String description;
}
