package com.feelmycode.parabole.domain;

import com.feelmycode.parabole.enumtype.CouponUseState;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.global.util.UuidApp;
import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Entity
@Table(name = "user_coupons")
@Getter
public class UserCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;

    @Column(name = "serial_no")
    private String serialNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name="coupon_id", referencedColumnName="coupon_id"),
        @JoinColumn(name="seller_id", referencedColumnName="seller_id")
    })
//    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "coupon_use_state")
    @NotNull
    @Enumerated(EnumType.STRING)
    private CouponUseState useState;

    @Column(name = "coupon_acquisition_date")
    @NotNull
    private LocalDateTime acquiredDate;

    @Column(name = "coupon_use_date")
    @NotNull
    private LocalDateTime useDate;

    /** Default Constructor */
    public UserCoupon() {
        this.serialNo = UuidApp.generator();
        this.coupon = null;
        this.user = null;
        this.useState = CouponUseState.NotUsed;
        this.acquiredDate = LocalDateTime.now();
        this.useDate = null;
    }

    /** 연관관계 편의 메서드 */
    public void setCoupon(Coupon coupon) {
        if (this.coupon != null) {
            this.coupon.getUserCoupons().remove(this);
        }
        this.coupon = coupon;
        coupon.getUserCoupons().add(this);
    }

    public void setUser(User user){
        if (this.user != null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "쿠폰에 배정된 사용자가 이미 존재합니다.");
//            this.user.getUserCoupons().remove(this);
        }
        this.user = user;
        user.getUserCoupons().add(this);
    }

    /** User used Coupon (Update Coupon useState, useDate) */
    public void useCoupon() {
        this.useState = CouponUseState.Used;
        this.useDate = LocalDateTime.now();
    }
}


