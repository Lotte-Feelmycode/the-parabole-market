package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class OrderInfoListDto {

    private Long userId;
    private List<OrderInfoSimpleDto> orderInfoDto;

}
