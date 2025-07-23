package com.bsdclinic.dto.response;

import com.bsdclinic.invoice.PurchasedService;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MedicalRecordUpdateResponse {
    private String patientName;
    private List<PurchasedService> purchasedServices;
    private BigDecimal servicesTotalPrice;
    private BigDecimal advance;
}
