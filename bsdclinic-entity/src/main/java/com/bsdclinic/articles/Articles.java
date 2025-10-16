package com.bsdclinic.articles;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.bsdclinic.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "articles")
public class Articles extends BaseEntity {
    @Id
    @Column(name = "article_id")
    private String articleId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "url_slug")
    private String urlSlug;

    @Column(name = "status")
    private String status;

    @PrePersist
    public void prePersist() {
        if (userId == null) {
            userId = NanoIdUtils.randomNanoId();
        }
    }
}
