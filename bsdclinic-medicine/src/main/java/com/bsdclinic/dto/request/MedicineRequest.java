package com.bsdclinic.dto.request;

import com.bsdclinic.validation.MedicineRuleAnnotation;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class MedicineRequest {
    @NotBlank(message = "{validation.required.medicine_title}")
    @Size(max = 255, message = "{validation.input.max_length.255}")
    private String title;

    @NotNull(message = "{validation.required.medicine_unit_price}")
    @DecimalMin(value = "0.0", message = "{validation.price.must_be_positive}")
    private BigDecimal unitPrice;

    @NotBlank(message = "{validation.required.medicine_unit}")
    @MedicineRuleAnnotation.ValidUnit(message = "{validation.invalid.medicine_unit}")
    private String unit;

    @Size(max = 1000, message = "{validation.input.max_length.1000}")
    private String dosage;

    @Size(max = 1000, message = "{validation.input.max_length.1000}")
    private String sideEffect;

    @MedicineRuleAnnotation.ValidCategoryIds(message = "{validation.invalid.category_ids}")
    private Set<String> categoryIds;
}
