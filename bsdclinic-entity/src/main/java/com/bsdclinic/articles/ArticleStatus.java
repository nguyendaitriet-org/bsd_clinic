package com.bsdclinic.articles;

import java.util.Arrays;
import java.util.List;

public enum ArticleStatus {
    PUBLISHED,
    DRAFT,
    HIDDEN;

    public static List<String> getAllNames() {
        return Arrays.stream(ArticleStatus.values()).map(ArticleStatus::name).toList();
    }
}
