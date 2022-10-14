package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class OrderInfoListDto {

    private List<OrderInfoSimpleDto> orderInfoDto;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(OrderInfoSimpleDto dto : orderInfoDto) {
            sb.append(dto.toString()).append("\n");
        }
        return sb.toString();
    }

}
