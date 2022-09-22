package com.feelmycode.parabole.domain.coupons;

import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.global.util.UuidApp;
import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "user_coupons")
@Getter
public class UserCoupon
//    extends BaseTimeEntity
{

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

    @Column(name = "coupon_use_state", nullable = false)
    @ColumnDefault("0")
    @NotNull
    private Integer useState;                            // 쿠폰 사용안함 0   사용완료 1

    @Column(name = "coupon_acquisition_date")
    @NotNull
    private String acquiredDate;

    @Column(name = "coupon_use_date")
    @NotNull
    private String useDate;


    /** 연관관계 편의 메서드 */
    public void setCoupon(Coupon coupon) {
        if (this.coupon != null) {
            this.coupon.getUserCoupons().remove(this);
        }
        this.coupon = coupon;
//        아래 코드는 연관관계 주인인 Coupon 에서 해준다
//        coupon.getUserCoupons().add(this);
    }

    public void setUser(User user) {
        this.user = user;
//        아래 코드는 연관관계 주인인 User 에서 해준다
//        user.getUserCoupons().add(this);
    }

    /** Default Constructor */
    public UserCoupon() {
        this.serialNo = UuidApp.generator();

        this.coupon = null;     // issueCoupon 에서 coupon 넣어줍니다
        this.user = null;
        this.useState = 0;
        this.acquiredDate = LocalDateTime.now().toString();
        this.useDate = null;
    }

//    public UserCoupon(Coupon coupon, User user) {
//        this.serialNo = UuidApp.generator();
//        this.coupon = coupon;
//        this.user = user;
//        this.useState = 0;
//        this.acquiredDate = LocalDateTime.now();
//        this.useDate = null;
//    }

    /** User used Coupon (Update Coupon useState, useDate */
    public void useCoupon(String usedDate) {
        this.useState = 1;
        this.useDate = usedDate;
    }
}


