package com.feelmycode.parabole.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum EventType {
    RAFFLE("RAFFLE","추첨", 1),
    FCFS("FCFS","선착순", 2);

    private String code;
    private String name;
    private Integer value;

    EventType(String code, String name, Integer value) {
        this.code = code;
        this.name = name;
        this.value = value;
    }
}
