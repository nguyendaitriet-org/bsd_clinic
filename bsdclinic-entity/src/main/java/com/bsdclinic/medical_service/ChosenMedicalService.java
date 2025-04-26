
package com.bsdclinic.medical_service;

import com.bsdclinic.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "chosen_medical_services")
@IdClass(ChosenMedicalServiceId.class)
public class ChosenMedicalService extends BaseEntity {
    @Id
    @Column(name = "medical_record_id")
    private String medicalRecordId;

    @Id
    @Column(name = "medical_service_id")
    private String medicalServiceId;

}
