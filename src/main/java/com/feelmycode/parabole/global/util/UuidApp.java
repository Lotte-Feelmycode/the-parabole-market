package com.feelmycode.parabole.global.util;

import java.util.UUID;

public class UuidApp {

    public static String generator() {

        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        return uuidStr;
    }

}