package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderInfoRequestListDto {

    private List<Long> orderInfoIdList;
    private String couponSerialNo;

    public OrderInfoRequestListDto(List<Long> orderInfoIdList, String couponSerialNo) {
        this.orderInfoIdList = orderInfoIdList;
        this.couponSerialNo = couponSerialNo;
    }
}
