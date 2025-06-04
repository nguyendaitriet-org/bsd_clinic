package com.bsdclinic.admin;

import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.dto.request.AppointmentFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AppointmentSpecifications {
    public static Specification<Appointment> withFilter(AppointmentFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            String subscriberId = filter.getSubscriberId();
            if (StringUtils.hasText(subscriberId)) {
                predicates.add(root.get("subscriberId").in(List.of(subscriberId)));
            }

            String keywordInput = resolveKeywordInput(filter);
            if (StringUtils.hasText(keywordInput)) {
                String keyword = "%" + keywordInput.toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("patientName")), keyword),
                    cb.like(cb.lower(root.get("patientPhone")), keyword),
                    cb.like(cb.lower(root.get("patientEmail")), keyword)
                ));
            }

            if (StringUtils.hasText(filter.getPatientPhone())) {
                predicates.add(cb.equal(root.get("patientPhone"), filter.getPatientPhone()));
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