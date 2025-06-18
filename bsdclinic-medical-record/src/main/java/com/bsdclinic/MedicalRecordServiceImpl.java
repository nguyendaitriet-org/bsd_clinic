package com.bsdclinic;

import com.bsdclinic.appointment.ActionStatus;
import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.dto.request.MedicalRecordUpdateRequest;
import com.bsdclinic.dto.response.MedicalRecordResponse;
import com.bsdclinic.exception_handler.exception.ConflictException;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.medical_record.MedicalRecord;
import com.bsdclinic.medical_service.ChosenMedicalService;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.repository.ChosenServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final ChosenServiceRepository chosenServiceRepository;
    private final MessageProvider messageProvider;
    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    @Transactional
    public MedicalRecordResponse createMedicalRecord(String appointmentId) {
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
    public MedicalRecordResponse getMedicalRecord(String medicalRecordId) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.medical_record")));
        return medicalRecordMapper.toDto(medicalRecord);
    }

    @Override
    @Transactional
    @Modifying
    public void updateMedicalRecordAndAppointment(String medicalRecordId, String appointmentId, MedicalRecordUpdateRequest request) {
        if (!medicalRecordRepository.existsByAppointmentIdAndMedicalRecordId(appointmentId, medicalRecordId)) {
            throw new NotFoundException(messageProvider.getMessage("validation.no_exist.medical_record"));
        }

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.appointment"))
        );

        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.medical_record"))
        );

        /* Update appointment */
        Appointment updatedAppointment = medicalRecordMapper.toAppointment(request, appointment);
        appointmentRepository.save(updatedAppointment);

        /* Update medical record */
        MedicalRecord updatedMedicalRecord = medicalRecordMapper.toEntity(request, medicalRecord);
        medicalRecordRepository.save(updatedMedicalRecord);

        /* Update the chosen medical service for medical record */
        chosenServiceRepository.deleteByMedicalRecordId(medicalRecordId);
        List<ChosenMedicalService> updatedChosenMedicalServices = request.getMedicalServiceIds().stream()
                .map(medicalServiceId -> new ChosenMedicalService()
                        .setMedicalRecordId(medicalRecordId)
                        .setMedicalServiceId(medicalServiceId))
                .toList();
        chosenServiceRepository.saveAll(updatedChosenMedicalServices);
    }
}
