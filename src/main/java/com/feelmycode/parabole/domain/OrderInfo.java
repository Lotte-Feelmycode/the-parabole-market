package com.feelmycode.parabole.domain;

import com.feelmycode.parabole.dto.OrderInfoResponseDto;
import com.feelmycode.parabole.enumtype.OrderInfoState;
import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "order_infos")
public class OrderInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_info_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_coupon_id")
    private UserCoupon userCoupon;

    // 배송의 상태
    @Column(name = "order_info_state")
    private Integer state;

    @NotNull
    @Column(name = "product_id")
    private Long productId;

    @NotNull
    @Column(name = "product_name")
    private String productName;

    @NotNull
    @Column(name = "product_cnt")
    private Integer productCnt;

    @NotNull
    @Column(name = "product_price")
    private Long productPrice;

    @NotNull
    @Column(name = "product_discount_price")
    private Long productDiscountPrice;

    @NotNull
    @Column(name = "seller_id")
    private Long sellerId;

    @NotNull
    @Column(name = "seller_store_name")
    private String sellerStoreName;

    public void setState(Integer state) {
        this.state = state;
    }

    public void setState(String state) {
        this.state = OrderInfoState.returnValueByName(state).getValue();
    }

    public void setState(OrderInfoState state) {
        this.state = state.getValue();
    }

    public void setUserCoupon(UserCoupon userCoupon) {
        this.userCoupon = userCoupon;
    }

    public OrderInfo(Order order, UserCoupon userCoupon, Long productId,
        String productName, Integer productCnt, Long productPrice, Long sellerId, String sellerStoreName) {
        this.order = order;
        this.userCoupon = userCoupon;
        this.productId = productId;
        this.productName = productName;
        this.productCnt = productCnt;
        this.productPrice = productPrice;
        this.sellerId = sellerId;
        this.sellerStoreName = sellerStoreName;
    }

    public OrderInfoResponseDto toDto() {
        return new OrderInfoResponseDto(id, OrderInfoState.returnNameByValue(state).getState(),
            order.getUser().getEmail(), productId, productName, productCnt, productPrice,
            productDiscountPrice, "", getUpdatedAt());
    }

}
