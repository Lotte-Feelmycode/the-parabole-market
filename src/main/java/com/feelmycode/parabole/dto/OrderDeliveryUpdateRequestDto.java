package com.feelmycode.parabole.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDeliveryUpdateRequestDto {

    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String receiverName;
    private String receiverPhone;
    private String addressSimple;
    private String addressDetail;
    private String deliveryComment;
    private String orderState;
    private String orderInfoState;
    private String payState;

}
