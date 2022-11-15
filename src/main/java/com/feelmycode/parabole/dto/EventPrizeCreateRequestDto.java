package com.feelmycode.parabole.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventPrizeCreateRequestDto {

    private Long id;
    private String type;
    private Integer stock;

    public EventPrizeCreateRequestDto(Long id, String type, Integer stock) {
        this.id = id;
        this.type = type;
        this.stock = stock;
    }
}
