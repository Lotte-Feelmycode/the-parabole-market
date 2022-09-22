package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EventPrizeCreateRequestDto {
    private String prizeType;
    private Integer stock;
    private List<Long> productIds;
    private List<Long> couponIds;
}
