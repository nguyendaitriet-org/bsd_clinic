package com.bsdclinic.repository;

import com.bsdclinic.dto.response.IMedicalRecordResponse;
import com.bsdclinic.medical_record.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, String>, JpaSpecificationExecutor<MedicalRecord> {
    boolean existsByMedicalRecordId(String medicalRecordId);

    boolean existsByAppointmentId(String appointmentId);

    boolean existsByAppointmentIdAndMedicalRecordId(String appointmentId, String medicalRecordId);

    @Query(value = """
        SELECT
            a.appointment_id AS appointmentId,
            m.medical_record_id AS medicalRecordId,
            a.patient_name AS patientName,
            a.patient_phone AS patientPhone,
            m.created_at AS createdAt,
            u.full_name AS doctorName
        FROM medical_records m
        JOIN appointments a ON m.appointment_id = a.appointment_id
        JOIN users u ON a.doctor_id = u.user_id
        WHERE
            (:keyword IS NULL OR
                LOWER(a.patient_name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(a.patient_phone) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:doctorIds IS NULL OR a.doctor_id IN (:doctorIds))
            AND ((:createdAtFrom)::TIMESTAMP IS NULL OR m.created_at >= (:createdAtFrom)::TIMESTAMP)
            AND ((:createdAtTo)::TIMESTAMP IS NULL OR m.created_at <= (:createdAtTo)::TIMESTAMP)
    """, nativeQuery = true)
    Page<IMedicalRecordResponse> findMedicalRecordWithFilters(
            @Param("keyword") String keyword,
            @Param("createdAtFrom") LocalDateTime createdAtFrom,
            @Param("createdAtTo") LocalDateTime createdAtTo,
            @Param("doctorIds") List<String> doctorIds,
            Pageable pageable
    );
}
