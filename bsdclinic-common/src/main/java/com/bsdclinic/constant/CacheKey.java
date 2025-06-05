package com.bsdclinic.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class CacheKey {
    public static final String CLINIC_INFO = "clinicInfo";
    public static final String USERS_FOR_SELECT = "usersForSelect";
}
