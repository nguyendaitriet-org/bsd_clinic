package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicineRequest;
import com.bsdclinic.medicine.Medicine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Override
    public void createMedicine(CreateMedicineRequest request) {
        Medicine medicine = medicineMapper.toEntity(request);
        medicineRepository.save(medicine);
    }
}
