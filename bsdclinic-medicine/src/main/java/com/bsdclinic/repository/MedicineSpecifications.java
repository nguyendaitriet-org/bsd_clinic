package com.bsdclinic.repository;

import com.bsdclinic.category.CategoryAssignment;
import com.bsdclinic.category.CategoryAssignment_;
import com.bsdclinic.dto.request.MedicineFilter;
import com.bsdclinic.medicine.Medicine;
import com.bsdclinic.medicine.Medicine_;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MedicineSpecifications {
    public static Specification<Medicine> withFilter(MedicineFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            String keywordInput = filter.getKeyword();
            if (StringUtils.hasText(keywordInput)) {
                String keyword = "%" + keywordInput.toLowerCase() + "%";
                predicates.add(cb.or(cb.like(cb.lower(root.get(Medicine_.TITLE)), keyword)));
            }

            List<String> categoryIds = filter.getCategoryIds();
            if (!CollectionUtils.isEmpty(categoryIds)) {
                Subquery<String> subquery = query.subquery(String.class);
                Root<CategoryAssignment> ca = subquery.from(CategoryAssignment.class);
                subquery.select(ca.get(CategoryAssignment_.ENTITY_ID))
                        .where(ca.get(CategoryAssignment_.CATEGORY_ID).in(categoryIds));
                predicates.add(root.get(Medicine_.MEDICINE_ID).in(subquery));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}