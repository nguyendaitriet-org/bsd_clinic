package com.bsdclinic.medical_service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "medical_services")
public class MedicalService {
    @Id
    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @PrePersist
    public void prePersist() {
        if (serviceId == null) {
            serviceId = NanoIdUtils.randomNanoId();
        }
    }
}
