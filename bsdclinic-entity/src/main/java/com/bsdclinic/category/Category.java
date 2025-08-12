package com.bsdclinic.category;

import com.bsdclinic.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    @Id
    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "title")
    private String title;

    @Column(name = "category_type")
    private String categoryType;
}
