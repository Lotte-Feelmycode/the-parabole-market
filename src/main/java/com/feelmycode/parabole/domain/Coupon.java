package com.feelmycode.parabole.domain;

import com.feelmycode.parabole.enumtype.CouponType;
import com.feelmycode.parabole.enumtype.CouponUseState;
import com.sun.istack.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

    @Column(name = "coupon_details")
    @NotNull
    private String detail;

    @Column(name = "coupon_publish_cnt")
    @NotNull
    private Integer cnt;                       // 발행할 쿠폰의 수량

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private List<UserCoupon> userCoupons = new ArrayList<>();

    public Coupon(String name, Seller seller, CouponType type, Integer discountValue,
        LocalDateTime validAt, LocalDateTime expiresAt, String detail, Integer cnt) {

        this.name = name;
        this.seller = seller;
        this.type = type;
        this.discountValue = discountValue;
        this.validAt = validAt;
        this.expiresAt = expiresAt;
        this.detail = detail;
        this.cnt = cnt;
    }

    public Coupon(Seller seller, LocalDateTime validAt, LocalDateTime expiresAt) {
        this.seller = seller;
        this.validAt = validAt;
        this.expiresAt = expiresAt;
    }

    public void addUserCoupon(UserCoupon userCoupon) {
//        userCoupon.setCoupon(this);
        this.userCoupons.add(userCoupon);
    }

    public int getUsedUserCouponCnt() {
        int cnt = 0;
        for (UserCoupon uc : userCoupons) {
            if (uc.getUseState().equals(CouponUseState.Used)) {
                cnt++;
            }
        }
        return cnt;
    }

    public int getNotUsedUserCouponCnt() {
        int cnt = 0;
        for (UserCoupon uc : userCoupons) {
            if (uc.getUseState().equals(CouponUseState.NotUsed)) {
                cnt++;
            }
        }
        return cnt;
    }

    public List<UserCoupon> getUsedUserCouponList() {
        return userCoupons.stream()
            .filter(uc -> CouponUseState.Used.equals(uc.getUseState()))
            .collect(Collectors.toList());
    }

    public List<UserCoupon> getNotUsedUserCouponList() {
        return userCoupons.stream()
            .filter(uc -> CouponUseState.NotUsed.equals(uc.getUseState()))
            .collect(Collectors.toList());
    }

    public List<UserCoupon> getNotAssignedUserCouponList() {
        List<UserCoupon> list = new ArrayList<>();
        for (UserCoupon uc : userCoupons) {
            if (uc.getUser() == null && uc.getUseState() == CouponUseState.NotUsed) {
                list.add(uc);
            }
        }
        return list;
    }

    public void setCouponForEvent(Integer inputStock) {
        userCoupons.stream().limit(inputStock)
            .filter(userCoupon -> userCoupon.getUseState().equals(CouponUseState.NotUsed))
            .forEach(UserCoupon::setEventEnrolled);
    }

    public void cancelCouponEvent(Integer inputStock) {
        userCoupons.stream().limit(inputStock)
            .filter(userCoupon -> userCoupon.getUseState().equals(CouponUseState.EventEnrolled))
            .forEach(UserCoupon::setNotUsed);
    }
}
