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
    @Column(name = "article_id", length = 50, nullable = false)
    private String articleId;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Column(name = "category_id ", length = 50, nullable = false)
    private String category_id ;

    @Column(name = "url", length = 255)
    private String url;

    @Column(name = "status", length = 10)
    private String status;

    @PrePersist
    public void prePersist() {
        if (userId == null) {
            userId = NanoIdUtils.randomNanoId();
        }
    }
}
