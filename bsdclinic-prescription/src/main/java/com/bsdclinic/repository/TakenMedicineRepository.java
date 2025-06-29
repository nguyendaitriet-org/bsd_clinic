package com.bsdclinic.repository;

import com.bsdclinic.medicine.TakenMedicine;
import com.bsdclinic.medicine.TakenMedicineId;
import com.bsdclinic.prescription.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TakenMedicineRepository extends JpaRepository<TakenMedicine, TakenMedicineId> {
    void deleteByPrescriptionId(String prescriptionId);
}
