package com.bsdclinic;

import com.bsdclinic.dto.TakenMedicineDto;
import com.bsdclinic.dto.request.CreatePrescriptionRequest;
import com.bsdclinic.dto.response.CreatePrescriptionResponse;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.medicine.TakenMedicine;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.prescription.Prescription;
import com.bsdclinic.repository.PrescriptionRepository;
import com.bsdclinic.repository.TakenMedicineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final TakenMedicineRepository takenMedicineRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MessageProvider messageProvider;
    private final PrescriptionMapper prescriptionMapper;

    @Override
    @Transactional
    public CreatePrescriptionResponse createPrescription(CreatePrescriptionRequest request) {
        String medicalRecordId = request.getMedicalRecordId();
        if (!medicalRecordRepository.existsByMedicalRecordId(medicalRecordId)) {
            throw new NotFoundException(messageProvider.getMessage("validation.no_exist.medical_record"));
        }

        Prescription prescription = prescriptionRepository.findByMedicalRecordId(medicalRecordId);
        /* Create new prescription for the medical record only when there is no prescription yet */
        if (prescription == null) {
            prescription = prescriptionMapper.toEntity(request);
            prescription = prescriptionRepository.save(prescription);
            String prescriptionId = prescription.getPrescriptionId();
            List<TakenMedicine> takenMedicines = request.getTakenMedicines().stream().map(item -> {
                TakenMedicine takenMedicine = prescriptionMapper.toTakenMedicine(item);
                takenMedicine.setPrescriptionId(prescriptionId);
                return takenMedicine;
            }).toList();
            takenMedicineRepository.saveAll(takenMedicines);
        }

        List<TakenMedicineDto> takenMedicineDtos = prescriptionRepository.getTakenMedicinesByPrescriptionId(prescription.getPrescriptionId());
        CreatePrescriptionResponse response = prescriptionMapper.toDto(prescription);
        response.setTakenMedicines(takenMedicineDtos);

        return response;
    }

    @Override
    @Transactional
    public void deletePrescription(String prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.prescription"))
        );
        takenMedicineRepository.deleteByPrescriptionId(prescriptionId);
        prescriptionRepository.delete(prescription);
    }
}
