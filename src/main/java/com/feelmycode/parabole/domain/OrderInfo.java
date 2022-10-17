package com.feelmycode.parabole.domain;

import com.feelmycode.parabole.dto.OrderInfoResponseDto;
import com.feelmycode.parabole.enumtype.OrderPayState;
import com.feelmycode.parabole.enumtype.OrderState;
import com.sun.istack.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

//    @OneToOne(mappedBy = "orderInfo", cascade = CascadeType.ALL)
//    private UserCoupon userCoupon;

    // 배송의 상태
    @Column(name = "order_info_state")
    private OrderState state;

    // 주문방식
    @Column(name = "order_info_pay_state")
    private OrderPayState payState;

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

    public OrderInfo(Order order, UserCoupon userCoupon, Integer state, String payState,
        Long productId, String productName, Integer productCnt,
        Long productPrice, Long productDiscountPrice, Long sellerId, String sellerStoreName) {
        this.order = order;
//        this.userCoupon = userCoupon;
        this.state = state;
        this.payState = OrderPayState.returnValueByName(payState);
        this.productId = productId;
        this.productName = productName;
        this.productCnt = productCnt;
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscountPrice;
        this.sellerId = sellerId;
        this.sellerStoreName = sellerStoreName;
    }

    public OrderInfo(Order order, String state, String payState, Long productId,
        String productName, Long productPrice) {
        this.order = order;
        this.state = OrderPayState.returnValueByName(state);
        this.payState = OrderPayState.returnValueByName(payState);
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public OrderInfo(Order order, String payState, Long productId,
        String productName, Integer productCnt, Long productPrice) {
        this.order = order;
        payState = payState.trim();
        this.payState = OrderPayState.returnValueByName(payState);
        this.productId = productId;
        this.productName = productName;
        this.productCnt = productCnt;
        this.productPrice = productPrice;
    }

    public OrderInfoResponseDto toDto() {
        return new OrderInfoResponseDto(id, OrderState.returnNameByValue(state),
            OrderState.returnNameByValue(payState), order.getUser().getId(),
            order.getUser().getEmail(), productId, productName, productCnt, productPrice,
            productDiscountPrice);
    }

}

