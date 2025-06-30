package com.bsdclinic.prescription;

import com.bsdclinic.BaseEntity;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "prescriptions")
public class Prescription extends BaseEntity {
    @Id
    @Column(name = "prescription_id")
    private String prescriptionId;

    @Column(name = "medical_record_id")
    private String medicalRecordId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "external_medicines", columnDefinition = "jsonb")
    @Type(JsonType.class)
    private List<ExternalMedicine> externalMedicines;

    @Column(name = "instruction")
    private String instruction;

    @Column(name = "re_examination")
    private String reExamination;

    @PrePersist
    public void prePersist() {
        if (prescriptionId == null) {
            prescriptionId = NanoIdUtils.randomNanoId();
        }
    }
}
