package com.bsdclinic.category;

import com.bsdclinic.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "category_assignments")
public class CategoryAssignment extends BaseEntity {
    @Id
    @Column(name = "category_assignment_id")
    private String categoryAssignmentId;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "entity_id")
    private String entityId;
}
