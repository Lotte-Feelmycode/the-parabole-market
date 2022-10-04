package com.feelmycode.parabole.global.util;

public class StringUtil {

    public static boolean controllerParamIsBlank(String inputString) {
        if(inputString == null || inputString.isEmpty() || inputString.isBlank()) return true;
        return inputString.equals("null");
    }

}
