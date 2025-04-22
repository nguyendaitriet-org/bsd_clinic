
package com.bsdclinic.medical_service;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "chosen_medical_services")
public class ChosenMedicalService {
    @Id
    @Column(name = "medical_record_id")
    private String medicalRecordId;

    @Id
    @Column(name = "medical_service_id")
    private String medicalServiceId;

    @Id
    @Column(name = "service_id")
    private String serviceId;
}
