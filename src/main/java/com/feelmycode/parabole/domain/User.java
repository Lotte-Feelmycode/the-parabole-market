package com.feelmycode.parabole.domain;

import com.feelmycode.parabole.global.error.exception.NotSellerException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Builder;
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

    @Column(name = "user_email", length = 200)
    private String email;

    @Column(name = "user_name", length = 200)
    private String username;

    @Column(name = "user_nickname", length = 200)
    private String nickname;

    @Column(name = "user_phone", length = 200)
    private String phone;

    @Column(name = "user_password", length = 200)
    private String password;

    @Column(name = "user_role", length = 200)
    private String role;

    @Column(name = "user_img_url", length = 200)
    private String imageUrl;

    @Column(name = "user_auth_provider", length = 200)
    private String authProvider;

    @JoinColumn(name = "seller_id")
    @OneToOne
    private Seller seller;

    @OneToMany(mappedBy = "user")
    private List<UserCoupon> userCoupons = new ArrayList<>();

    /** Admin 에서 판매자 권한 부여 */
    public void setSeller(Seller seller) {
        seller.setUser(this);
        this.seller = seller;
    }

    public Seller getSeller() {
        if (seller == null) {
            throw new NotSellerException();
        }
        return seller;
    }

    public boolean sellerIsNull() {
        if (seller == null) {
            return true;
        }
        return false;
    }

    public User(String email, String name,
        String nickname, String phone, String password) {
        this.email = email;
        this.username = name;
        this.nickname = nickname;
        this.phone = phone;
        this.password = password;
    }

    @Builder
    public User(Long id, String email, String username, String nickname, String phone,
        String password,
        String role, String imageUrl, String authProvider, Seller seller,
        List<UserCoupon> userCoupons) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.nickname = nickname;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.imageUrl = imageUrl;
        this.authProvider = authProvider;
        this.seller = seller;
        this.userCoupons = userCoupons;
    }

}
