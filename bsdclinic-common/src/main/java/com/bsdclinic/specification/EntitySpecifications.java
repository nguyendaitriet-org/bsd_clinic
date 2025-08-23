package com.bsdclinic.specification;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic JPA Specification builder for filtering entities.
 *
 * Supports:
 *  - Keyword search on a given title field
 *  - Filtering by category assignments via subquery
 *
 * Usage example:
 *   Specification<Medicine> spec = EntitySpecifications.withFilter(
 *       filter,
 *       "medicineId",     // entityIdAttr
 *       "title",          // titleAttr
 *       CategoryAssignment.class  // category assignment class
 *   );
 *
 * This way, the same logic can be reused for other entities like MedicalService, etc.
 */
public class EntitySpecifications {
    public static <T, CA> Specification<T> withFilter(
            EntityFilter filter,
            String entityIdAttr,
            String titleAttr,
            Class<CA> categoryAssignmentClass
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(filter.getKeyword())) {
                String keyword = "%" + filter.getKeyword().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get(titleAttr)), keyword));
            }

            if (!CollectionUtils.isEmpty(filter.getCategoryIds())) {
                Subquery<String> subquery = query.subquery(String.class);
                Root<CA> ca = subquery.from(categoryAssignmentClass);
                subquery.select(ca.get("entityId"))
                        .where(ca.get("categoryId").in(filter.getCategoryIds()));
                predicates.add(root.get(entityIdAttr).in(subquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
