package com.bsdclinic.medical_record;

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
@Table(name = "medical_records")
public class MedicalRecord extends BaseEntity {
    @Id
    @Column(name = "medical_record_id")
    private String medicalRecordId;

    @Column(name = "appointment_id")
    private String appointmentId;

    @Column(name = "patient_address")
    private String patientAddress;

    @Column(name = "medical_history")
    private String medicalHistory;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "advance")
    private BigDecimal advance;

    @PrePersist
    public void prePersist() {
        if (medicalRecordId == null) {
            medicalRecordId = NanoIdUtils.randomNanoId();
        }
    }
}
