package com.bsdclinic.admin;

import com.bsdclinic.appointment.ActionStatus;
import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.appointment.Appointment_;
import com.bsdclinic.dto.request.AppointmentFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentSpecifications {
    public static Specification<Appointment> withFilter(AppointmentFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            String subscriberId = filter.getSubscriberId();
            if (StringUtils.hasText(subscriberId)) {
                predicates.add(cb.equal(root.get(Appointment_.SUBSCRIBER_ID), subscriberId));
            }

            String keywordInput = resolveKeywordInput(filter);
            if (StringUtils.hasText(keywordInput)) {
                String keyword = "%" + keywordInput.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get(Appointment_.PATIENT_NAME)), keyword),
                        cb.like(cb.lower(root.get(Appointment_.PATIENT_PHONE)), keyword),
                        cb.like(cb.lower(root.get(Appointment_.PATIENT_EMAIL)), keyword)
                ));
            }

            if (StringUtils.hasText(filter.getPatientPhone())) {
                predicates.add(cb.equal(root.get(Appointment_.PATIENT_PHONE), filter.getPatientPhone()));
            }

            List<String> doctorIds = filter.getDoctorIds();
            if (doctorIds != null && !doctorIds.isEmpty()) {
                predicates.add(root.get(Appointment_.DOCTOR_ID).in(doctorIds));
            }

            List<String> actionStatus = filter.getActionStatus();
            List<String> statusForDoctor = filter.getStatusForDoctor();
            boolean actionStatusExists = actionStatus != null && !actionStatus.isEmpty();
            boolean statusForDoctorExists = statusForDoctor != null && !statusForDoctor.isEmpty();
            if (actionStatusExists) {
                predicates.add(root.get(Appointment_.ACTION_STATUS).in(actionStatus));
            }
            if (!actionStatusExists && statusForDoctorExists) {
                predicates.add(root.get(Appointment_.ACTION_STATUS).in(statusForDoctor));
            }

            LocalDate registerDateFrom = filter.getRegisterDateFrom();
            if (registerDateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(Appointment_.REGISTER_DATE), registerDateFrom));
            }

            LocalDate registerDateTo = filter.getRegisterDateTo();
            if (registerDateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(Appointment_.REGISTER_DATE), registerDateTo));
            }

            /* Filter for doctor */
            /* Get all doctor's appointments for ADMIN role and only current doctor's for DOCTOR role */
            /* Use doctor ID presence to mark the case for doctor */
            String doctorId = filter.getDoctorId();
            if (StringUtils.hasText(doctorId)) {
                if (filter.isAdminRole()) {
                    predicates.add(cb.isNotNull(root.get(Appointment_.DOCTOR_ID)));
                } else {
                    predicates.add(cb.equal(root.get(Appointment_.DOCTOR_ID), doctorId));
                }
            }

            if (query != null) {
                query.orderBy(
                        // First order by: PENDING status first then ACCEPTED, CHECKED_IN (using CASE WHEN)
                        cb.asc(cb.selectCase()
                                .when(cb.equal(root.get(Appointment_.ACTION_STATUS), ActionStatus.PENDING.name()), 0)
                                .when(cb.equal(root.get(Appointment_.ACTION_STATUS), ActionStatus.ACCEPTED.name()), 1)
                                .when(cb.equal(root.get(Appointment_.ACTION_STATUS), ActionStatus.CHECKED_IN.name()), 2)
                                .otherwise(3)),
                        // Second order by: latest registerDate (descending)
                        cb.desc(root.get(Appointment_.REGISTER_DATE))
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String resolveKeywordInput(AppointmentFilter filter) {
        if (filter.getSearch() != null && StringUtils.hasText(filter.getSearch().getValue())) {
            return filter.getSearch().getValue();
        }
        if (StringUtils.hasText(filter.getKeyword())) {
            return filter.getKeyword();
        }
        return "";
    }
}