package com.feelmycode.parabole.domain;

import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.service.SellerService;
import com.sun.istack.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "coupons")
public class Coupon extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Column(name = "coupon_name", length = 500)
    @NotNull
    private String name;

    @Column(name = "coupon_type")
    @NotNull
//    @Enumerated(EnumType.STRING)
//    private CouponType type;
    private Integer type;                       // 쿠폰종류 ( 할인율1  할인금액2 )

    @Column(name = "coupon_discount_rate")
    @NotNull
    private Integer discountRate;               // 할인율

    @Column(name = "coupon_discount_amount")
    @NotNull
    private Long discountAmount;                // 할인금액(원)

    @Column(name = "coupon_valid_at")
    @NotNull
    private LocalDateTime validAt;

    @Column(name = "coupon_expires_at")
    @NotNull
    private LocalDateTime expiresAt;

    @Column(name = "coupon_max_discount_amount")
    @NotNull
    private Long maxDiscountAmount;

    @Column(name = "coupon_min_payment_amount")
    @NotNull
    private Long minPaymentAmount;

    @Column(name = "coupon_details")
    @NotNull
    private String detail;

    @Column(name = "coupon_publish_cnt")
    @NotNull
    private Integer cnt;                       // 발행할 쿠폰의 수량

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private List<UserCoupon> userCoupons = new ArrayList<>();

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Coupon(String name, Seller seller, Integer type, Integer discountRate,
        Long discountAmount, LocalDateTime validAt, LocalDateTime expiresAt,
        Long maxDiscountAmount, Long minPaymentAmount, String detail, Integer cnt) {

        this.name = name;
        this.seller = seller;
        this.type = type;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.validAt = validAt;
        this.expiresAt = expiresAt;
        this.maxDiscountAmount = maxDiscountAmount;
        this.minPaymentAmount = minPaymentAmount;
        this.detail = detail;
        this.cnt = cnt;
    }

    /**
     * Print all and each UserCoupon with SNo (quantity : cnt)
     */
    public void addUserCoupon(UserCoupon userCoupon) {
        userCoupon.setCoupon(this);
        getUserCoupons().add(userCoupon);
    }
}
