package com.bsdclinic.repository;

import com.bsdclinic.dto.TakenMedicineDto;
import com.bsdclinic.prescription.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, String> {
    Prescription findByMedicalRecordId(String medicalRecordId);

    @Query("SELECT new com.bsdclinic.dto.TakenMedicineDto(" +
                "m.title," +
                "t.purchasedQuantity," +
                "m.unitPrice," +
                "t.purchasedTotalPrice" +
            ") " +
            "FROM Medicine m " +
            "INNER JOIN TakenMedicine t ON m.medicineId = t.medicineId " +
            "WHERE m.medicineId IN :medicineIds ")
    List<TakenMedicineDto> getTakenMedicinesByMedicineIds(List<String> medicineIds);
}
