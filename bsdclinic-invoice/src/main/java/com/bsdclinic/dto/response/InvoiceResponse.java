package com.bsdclinic.dto.response;

import com.bsdclinic.invoice.PurchasedMedicine;
import com.bsdclinic.invoice.PurchasedService;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class InvoiceResponse {
    private String invoiceId;
    private String medicalRecordId;
    private Instant createdAt;
    private String patientName;
    private List<PurchasedService> purchasedServices;
    private BigDecimal servicesTotalPrice;
    private BigDecimal advance;
    private List<PurchasedMedicine> purchasedMedicines;
    private BigDecimal medicinesTotalPrice;
    private BigDecimal grandTotalPrice;
    private BigDecimal remainingPrice;
}
