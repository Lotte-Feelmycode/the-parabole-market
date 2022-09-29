package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.domain.OrderInfo;
import com.feelmycode.parabole.domain.UserCoupon;
import lombok.Getter;

@Getter
public class OrderInfoListDto {
    private Long userId;
    private String productName;
    private int productCnt;
    private Long productPrice;
    private Long productDiscountPrice;
    private Order order;

    public OrderInfo toEntity(Order order) {
        this.order = order;
        // TODO: 쿠폰 추가하기
        return new OrderInfo(order, new UserCoupon(), productName, productCnt, productPrice, productDiscountPrice);
    }

}
