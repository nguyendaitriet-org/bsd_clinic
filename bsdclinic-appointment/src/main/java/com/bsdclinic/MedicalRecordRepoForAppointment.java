package com.bsdclinic;

import com.bsdclinic.medical_record.MedicalRecord;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface MedicalRecordRepoForAppointment extends JpaRepository<MedicalRecord, String> {
    @Query("SELECT m.appointmentId, m.medicalRecordId FROM MedicalRecord m " +
            "WHERE m.appointmentId IN :appointmentIds")
    List<Tuple> findMedicalRecordIdsTuple(List<String> appointmentIds);

    default Map<String, String> findMedicalRecordIdsAsMap(List<String> appointmentIds) {
        return findMedicalRecordIdsTuple(appointmentIds).stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, String.class),
                        tuple -> tuple.get(1, String.class)
                ));
    }
}
