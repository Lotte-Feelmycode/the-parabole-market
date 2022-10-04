package com.feelmycode.parabole.global.util;

public class StringUtil {

    public static boolean controllerParamIsBlank(String inputString) {
 return inputString == null || inputString.isEmpty() || inputString.isBlank() || inputString.equals("null");
    }

}
