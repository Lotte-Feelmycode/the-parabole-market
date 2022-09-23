package com.feelmycode.parabole.domain;

import com.feelmycode.parabole.domain.coupons.Coupon;
import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sellers")
public class Seller {

    // TODO : 미완성 Entity 정식 버전 필요

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @Column(name = "seller_name", length = 200)
    @NotNull
    private String name;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Coupon> coupons = new ArrayList<>();

    /** Add coupon to seller */
    public void addCoupon(Coupon coupon) {
        coupon.setSeller(this);     // owner
        getCoupons().add(coupon);   // 관계설정
    }

}