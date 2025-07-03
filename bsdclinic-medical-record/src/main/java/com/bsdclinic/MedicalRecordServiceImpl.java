package com.bsdclinic;

import com.bsdclinic.appointment.ActionStatus;
import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.constant.DateTimePattern;
import com.bsdclinic.dto.request.MedicalRecordFilter;
import com.bsdclinic.dto.request.MedicalRecordUpdateRequest;
import com.bsdclinic.dto.response.IMedicalRecordResponse;
import com.bsdclinic.dto.response.MedicalRecordListResponse;
import com.bsdclinic.dto.response.MedicalRecordResponse;
import com.bsdclinic.exception_handler.exception.BadRequestException;
import com.bsdclinic.exception_handler.exception.ConflictException;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.medical_record.MedicalRecord;
import com.bsdclinic.medical_service.ChosenMedicalService;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.repository.ChosenServiceRepository;
import com.bsdclinic.response.DatatableResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Map;

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
    public DatatableResponse getMedicalRecordsByFilter(MedicalRecordFilter medicalRecordFilter) {
        adjustMedicalRecordFilter(medicalRecordFilter);

        Pageable pageable = PageRequest.of(
                medicalRecordFilter.getStart() / medicalRecordFilter.getLength(),
                medicalRecordFilter.getLength(),
                Sort.by(Sort.Direction.DESC, BaseEntity_.CREATED_AT)
        );
        Page<IMedicalRecordResponse> medicalRecords = medicalRecordRepository.findMedicalRecordWithFilters(
                medicalRecordFilter.getKeyword(),
                medicalRecordFilter.getCreatedAtFromDateTime(),
                medicalRecordFilter.getCreatedAtToDateTime(),
                medicalRecordFilter.getDoctorIds(),
                pageable
        );

        DatatableResponse<MedicalRecordListResponse> datatableResponse = new DatatableResponse<>();
        datatableResponse.setData(medicalRecordMapper.toMedicalRecordListResponses(medicalRecords.getContent()));
        datatableResponse.setDraw(medicalRecordFilter.getDraw());
        Long totalRecord = medicalRecords.getTotalElements();
        datatableResponse.setRecordsFiltered(totalRecord);
        datatableResponse.setRecordsTotal(totalRecord);

        return datatableResponse;
    }

    private void adjustMedicalRecordFilter(MedicalRecordFilter medicalRecordFilter) {
        List<String> doctorIds = medicalRecordFilter.getDoctorIds();
        if (doctorIds == null) {
            medicalRecordFilter.setDoctorIds(List.of());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimePattern.DEFAULT_DATE)
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            String createdAtFrom = medicalRecordFilter.getCreatedAtFrom();
            if (StringUtils.isBlank(createdAtFrom)) return;
            medicalRecordFilter.setCreatedAtFromDateTime(LocalDate.parse(createdAtFrom, formatter).atStartOfDay());

            String createdAtTo = medicalRecordFilter.getCreatedAtTo();
            if (StringUtils.isBlank(createdAtTo)) return;
            medicalRecordFilter.setCreatedAtToDateTime(LocalDate.parse(createdAtTo, formatter).atTime(LocalTime.MAX));
        } catch (DateTimeParseException e) {
            throw new BadRequestException(Map.of());
        }
    }

    @Override
    public MedicalRecordResponse getMedicalRecord(String medicalRecordId) {
        return medicalRecordRepository.getMedicalRecordResponse(medicalRecordId).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.medical_record"))
        );
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

    @Override
    @Transactional
    @Modifying
    public void deleteMedicalRecord(String medicalRecordId, String appointmentId) {
        // TODO: Handle deleting corresponding invoice and prescription
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.appointment"))
        );

        String actionStatus = appointment.getActionStatus();
        if (actionStatus.equals(ActionStatus.EXAMINING.name()) || actionStatus.equals(ActionStatus.ADVANCED.name())) {
            MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId).orElseThrow(
                    () -> new NotFoundException(messageProvider.getMessage("validation.no_exist.medical_record")));

            medicalRecordRepository.delete(medicalRecord);
            chosenServiceRepository.deleteByMedicalRecordId(medicalRecordId);
            appointmentRepository.updateActionStatus(appointmentId, ActionStatus.CHECKED_IN.name());
            return;
        }

        throw new ConflictException(messageProvider.getMessage("message.medical_record.delete.valid_status"));
    }
}
