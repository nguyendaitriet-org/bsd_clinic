package com.bsdclinic.medical_service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "medical_services")
public class MedicalService {
    @Id
    @Column(name = "medical_service_id")
    private String serviceId;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description")
    private String description;
    @PrePersist
    public void prePersist() {
        if (serviceId == null) {
            serviceId = NanoIdUtils.randomNanoId();
        }
    }
}
