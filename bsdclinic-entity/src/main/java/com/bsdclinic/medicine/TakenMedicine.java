package com.bsdclinic.medicine;

import com.bsdclinic.BaseEntity;
import com.bsdclinic.medicine.TakenMedicineId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "taken_medicines")
@IdClass(TakenMedicineId.class)
public class TakenMedicine extends BaseEntity {
    @Id
    @Column(name = "prescription_id")
    private String prescriptionId;

    @Id
    @Column(name = "medicine_id")
    private String medicineId;

    @Column(name = "purchased_quantity")
    private Integer purchasedQuantity;

    @Column(name = "purchased_total_price")
    private BigDecimal purchasedTotalPrice;
}
