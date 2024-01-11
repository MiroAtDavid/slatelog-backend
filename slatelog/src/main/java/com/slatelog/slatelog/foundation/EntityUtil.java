package com.slatelog.slatelog.foundation;

import java.util.UUID;
public abstract class EntityUtil {
    public static String generateUUIDv4(){
        return UUID.randomUUID().toString();
    }
}
