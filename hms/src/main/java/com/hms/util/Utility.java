package com.hms.util;

import java.util.UUID;

public class Utility {

    public static final String DOCTOR = "DOC";
    public static final String PATIENT = "PTN";
    public static final String GEN = "GEN";

    public static String generateId(String user) {
        return "HMS-" + user + UUID.randomUUID();
    }

    public static String generateId() {
        return generateId(GEN);
    }

}
