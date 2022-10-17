package com.feelmycode.parabole.domain;

import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.enumtype.CouponType;
import com.feelmycode.parabole.enumtype.CouponUseState;
import com.feelmycode.parabole.service.SellerService;
import com.sun.istack.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Column(name = "coupon_discount_value")
    @NotNull
    private Integer discountValue;

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

//    public void setSeller(Seller seller) {
//        this.seller = seller;
//    }

    public Coupon(String name, Seller seller, CouponType type, Integer discountValue,
        LocalDateTime validAt, LocalDateTime expiresAt,
        Long maxDiscountAmount, Long minPaymentAmount, String detail, Integer cnt) {

        this.name = name;
        this.seller = seller;
        this.type = type;
        this.discountValue = discountValue;
        this.validAt = validAt;
        this.expiresAt = expiresAt;
        this.maxDiscountAmount = maxDiscountAmount;
        this.minPaymentAmount = minPaymentAmount;
        this.detail = detail;
        this.cnt = cnt;
    }

    public void addUserCoupon(UserCoupon userCoupon) {
//        userCoupon.setCoupon(this);
        this.userCoupons.add(userCoupon);
    }

    public int getUsedUserCouponCnt() {
        int cnt = 0;
        for (UserCoupon uc : userCoupons) {
            if(uc.getUseState().equals(CouponUseState.Used)){
                cnt++;
            }
        }
        return cnt;
    }

    public int getNotUsedUserCouponCnt() {
        int cnt = 0;
        for (UserCoupon uc : userCoupons) {
            if(uc.getUseState().equals(CouponUseState.NotUsed)){
                cnt++;
            }
        }
        return cnt;
    }

    public List<UserCoupon> getUsedUserCouponList() {
        List<UserCoupon> list = new ArrayList<>();
        for (UserCoupon uc : userCoupons) {
            if(uc.getUseState() == CouponUseState.Used){
                list.add(uc);
            }
        }
        return list;
    }

    public List<UserCoupon> getNotUsedUserCouponList() {
        List<UserCoupon> list = new ArrayList<>();
        for (UserCoupon uc : userCoupons) {
            if(uc.getUseState() == CouponUseState.NotUsed){
                list.add(uc);
            }
        }
        return list;
    }

    public List<UserCoupon> getNotAssignedUserCouponList() {
        List<UserCoupon> list = new ArrayList<>();
        for (UserCoupon uc : userCoupons) {
            if(uc.getUser() == null){
                list.add(uc);
            }
        }
        return list;
    }
}
