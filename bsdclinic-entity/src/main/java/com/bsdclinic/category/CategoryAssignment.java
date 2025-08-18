package com.bsdclinic.category;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "category_assignments")
public class CategoryAssignment {
    @Id
    @Column(name = "category_assignment_id")
    private String categoryAssignmentId;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "entity_title")
    private String entityTitle;

    @PrePersist
    public void prePersist() {
        if (categoryAssignmentId == null) {
            categoryAssignmentId = NanoIdUtils.randomNanoId();
        }
    }
}
