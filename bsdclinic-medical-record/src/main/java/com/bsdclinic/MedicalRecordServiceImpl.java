package com.bsdclinic;

import com.bsdclinic.appointment.ActionStatus;
import com.bsdclinic.exception_handler.exception.ConflictException;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.medical_record.MedicalRecord;
import com.bsdclinic.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final MessageProvider messageProvider;
    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    @Transactional
    public MedicalRecordDto createMedicalRecord(String appointmentId) {
        if (!appointmentRepository.existsByAppointmentId(appointmentId)) {
            throw new NotFoundException(messageProvider.getMessage("validation.no_exist.appointment"));
        }

        if (medicalRecordRepository.existsByAppointmentId(appointmentId)) {
            throw new ConflictException(messageProvider.getMessage("validation.existed.medical_record"));
        }

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setAppointmentId(appointmentId);

        medicalRecord = medicalRecordRepository.save(medicalRecord);

        appointmentRepository.updateActionStatus(appointmentId, ActionStatus.EXAMINING.name());

        return medicalRecordMapper.toDto(medicalRecord);
    }

    @Override
    public MedicalRecordDto getMedicalRecord(String medicalRecordId) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId).orElseThrow(
                ()-> new NotFoundException(messageProvider.getMessage("validation.no_exist.medical_record")));
        return medicalRecordMapper.toDto(medicalRecord);
    }
}
