package com.bsdclinic.dto.request;

import com.bsdclinic.invoice.PurchasedMedicine;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreateInvoiceRequest {
    private String medicalRecordId;

    @NotBlank(message = "{validation.required.full_name}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String patientName;

    private List<PurchasedMedicine> purchasedMedicines;

    @DecimalMin(value = "0.0", message = "{validation.price.must_be_positive}")
    private BigDecimal medicinesTotalPrice;
}

