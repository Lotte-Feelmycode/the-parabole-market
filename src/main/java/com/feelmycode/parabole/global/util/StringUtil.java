package com.feelmycode.parabole.global.util;

public class StringUtil {

    public static final String ASCII_DOC_OUTPUT_DIR = "./src/docs/asciidoc/snippets";

    public static boolean controllerParamIsBlank(String inputString) {
 return inputString == null || inputString.isEmpty() || inputString.isBlank() || inputString.equals("null");
    }

}
