package com.bsdclinic.medicine;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.bsdclinic.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "medicines")
public class Medicine extends BaseEntity {

    @Id
    @Column(name = "medicine_id")
    private String medicineId;

    @Column(name = "title")
    private String title;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "unit")
    private String unit;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "side_effect")
    private String sideEffect;

    @PrePersist
    public void prePersist() {
        if (medicineId == null) {
            medicineId = NanoIdUtils.randomNanoId();
        }
    }
}
