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

            if (StringUtils.hasText(filter.getKeyword())) {
                String keyword = "%" + filter.getKeyword().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("patientName")), keyword),
                    cb.like(cb.lower(root.get("patientPhone")), keyword),
                    cb.like(cb.lower(root.get("patientEmail")), keyword)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}