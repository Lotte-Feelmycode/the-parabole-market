package com.feelmycode.parabole.domain;

import com.feelmycode.parabole.domain.coupons.UserCoupon;
import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email", length = 200, nullable = false)
    @NotNull
    private String email;

    @Column(name = "user_name", length = 200, nullable = false)
    @NotNull
    private String name;

    @Column(name = "user_nickname", length = 200, nullable = false)
    @NotNull
    private String nickname;

    @Column(name = "user_password", length = 200, nullable = false)
    @NotNull
    private String password;

    @Column(name = "user_created_at", nullable = false)
    @NotNull
    private String createdAt;

    @Column(name = "user_updated_at", nullable = false)
    @NotNull
    private String updatedAt;

    @Column(name = "user_role", nullable = false)
    @NotNull
    private String role;

    @Column(name = "img_url", nullable = false)
    @NotNull
    private String imgUrl;

    @OneToMany(mappedBy = "user")
    private List<UserCoupon> userCoupons = new ArrayList<>();

    /** 연관관계 메서드 */
    public void setUserCoupon(UserCoupon userCoupon){
        userCoupon.setUser(this);               // owner
        this.getUserCoupons().add(userCoupon);
    }
}
