package com.bsdclinic.medicine;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "taken_medicines")
@IdClass(TakenMedicineId.class)
public class TakenMedicine {
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

    @Column(name = "usage")
    private String usage;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;
}
