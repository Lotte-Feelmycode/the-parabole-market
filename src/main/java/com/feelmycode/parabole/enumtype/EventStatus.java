package com.feelmycode.parabole.enumtype;

import java.util.Arrays;
import lombok.Getter;
import java.util.List;

@Getter
public enum EventStatus {
    BEFORE("BEFORE", "진행전", 0),
    INPROGRESS("INPROGRESS", "진행중", 1),
    END("END", "종료", 2);

    private String code;
    private String name;
    private Integer value;

    EventStatus(String code, String name, Integer value) {
        this.code = code;
        this.name = name;
        this.value = value;
    }

    public static boolean isUpdatable(Integer value) {
        return value == 0 ? true: false;
    }

}
