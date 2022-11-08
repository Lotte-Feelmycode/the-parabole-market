package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderInfoListDto {

    private Long userId;
    private List<OrderInfoSimpleDto> orderInfoDto;

    public OrderInfoListDto(Long userId, List<OrderInfoSimpleDto> orderInfoSimpleDtoLost) {
        this.userId = userId;
        this.orderInfoDto = orderInfoSimpleDtoLost;
    }
}
