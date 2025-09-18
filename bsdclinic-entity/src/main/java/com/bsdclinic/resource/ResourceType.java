package com.bsdclinic.resource;

import java.util.Arrays;
import java.util.List;

public enum ResourceType {
    VIDEO,
    IMAGE,
    AUDIO,
    DOCUMENT,
    OTHER;

    public static List<String> getAllNames() {
        return Arrays.stream(ResourceType.values()).map(ResourceType::name).toList();
    }
}
