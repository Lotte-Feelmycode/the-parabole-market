package com.feelmycode.parabole.domain;

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

    @NotNull
    @Column(name = "product_name")
    private String productName;

    @NotNull
    @Column(name = "product_cnt")
    private int productCnt;

    @NotNull
    @Column(name = "product_price")
    private Long productPrice;

    @NotNull
    @Column(name = "product_discount_price")
    private Long productDiscountPrice;

    public OrderInfo(Order order) {
        this.order = order;
    }

    public OrderInfo(Order order, UserCoupon userCoupon, String productName, int productCnt,
        Long productPrice, Long productDiscountPrice) {
        this.order = order;
//        this.userCoupon = userCoupon;
        this.productName = productName;
        this.productCnt = productCnt;
        this.productPrice = productPrice;
        this.productDiscountPrice = productDiscountPrice;
    }

}
