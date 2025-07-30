package com.bsdclinic.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class ComponentName {
    public static final String LOCAL_FILE_STORAGE = "localFileStorage";
    public static final String S3_FILE_STORAGE = "s3FileStorage";
}
