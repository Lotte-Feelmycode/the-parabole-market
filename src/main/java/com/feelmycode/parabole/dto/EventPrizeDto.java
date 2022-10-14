package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.Product;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventPrizeDto {

    private Long eventPrizeId;
    private String prizeType;
    private Integer stock;
    private Long productId;
    private String productName;
    private String productImg;
    private Long couponId;
    private String couponDetail;
    private Integer couponDiscountValue;
    private LocalDateTime expiresAt;

    public EventPrizeDto(EventPrize eventPrize) {
        Product product = eventPrize.getProduct();
        this.eventPrizeId = eventPrize.getId();
        this.prizeType = eventPrize.getPrizeType();
        if (this.prizeType.equals("COUPON")) {
            Coupon coupon = eventPrize.getCoupon();
            couponId = coupon.getId();
            couponDetail = coupon.getDetail();
            couponDiscountValue = coupon.getDiscountValue();
            this.stock = eventPrize.getStock();
            this.expiresAt = coupon.getExpiresAt();
        } else if (this.prizeType.equals("PRODUCT")) {
            this.productId = product.getId();
            this.stock = eventPrize.getStock();
            this.productName = product.getName();
            this.productImg = product.getThumbnailImg();
        }
    }

}
