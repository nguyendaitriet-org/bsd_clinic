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
@Table(name = "categories")
public class Category {
    @Id
    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "title")
    private String title;

    @Column(name = "category_type")
    private String categoryType;

    @PrePersist
    public void prePersist() {
        if (categoryId == null) {
            categoryId = NanoIdUtils.randomNanoId();
        }
    }
}
