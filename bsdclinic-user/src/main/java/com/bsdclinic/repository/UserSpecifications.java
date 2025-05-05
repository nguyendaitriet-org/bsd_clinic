package com.bsdclinic.repository;

import com.bsdclinic.dto.request.UserFilter;
import com.bsdclinic.user.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {

    public static Specification<User> withFilter(UserFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            /* Exclude the current logged-in user */
            predicates.add(cb.not(root.get("id").in(List.of(filter.getCurrentUserId()))));

            if (StringUtils.hasText(filter.getKeyword())) {
                String keyword = "%" + filter.getKeyword().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("fullName")), keyword),
                    cb.like(cb.lower(root.get("email")), keyword),
                    cb.like(cb.lower(root.get("phone")), keyword)
                ));
            }

            if (filter.getRoleIds() != null && !filter.getRoleIds().isEmpty()) {
                predicates.add(root.get("roleId").in(filter.getRoleIds()));
            }

            if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
                predicates.add(root.get("status").in(filter.getStatus()));
            }

            ZoneId zone = ZoneId.systemDefault();
            if (filter.getCreatedFrom() != null) {
                Instant from = filter.getCreatedFrom().atStartOfDay(zone).toInstant();
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from));
            }

            if (filter.getCreatedTo() != null) {
                Instant to = filter.getCreatedTo().atTime(LocalTime.MAX).atZone(zone).toInstant();
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), to));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}