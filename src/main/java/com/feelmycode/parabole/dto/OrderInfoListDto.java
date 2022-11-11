package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderInfoListDto {

    private List<OrderInfoSimpleDto> orderInfoDto;

}
