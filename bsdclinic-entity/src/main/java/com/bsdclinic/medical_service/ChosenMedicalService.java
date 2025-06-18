
package com.bsdclinic.medical_service;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "chosen_medical_services")
@IdClass(ChosenMedicalServiceId.class)
public class ChosenMedicalService {
    @Id
    @Column(name = "medical_record_id")
    private String medicalRecordId;

    @Id
    @Column(name = "medical_service_id")
    private String medicalServiceId;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;
}
