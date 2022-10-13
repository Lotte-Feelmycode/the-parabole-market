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
import javax.validation.constraints.NotNull;
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
    @NotNull
    private String email;

    @Column(name = "user_name", length = 200)
    @NotNull
    private String name;

    @Column(name = "user_nickname", length = 200)
    @NotNull
    private String nickname;

    @Column(name = "user_phone", length = 200)
    @NotNull
    private String phone;

    @Column(name = "user_password", length = 200)
    @NotNull
    private String password;

    @JoinColumn(name = "seller_id")
    @OneToOne
    private Seller seller;

    @JoinColumn(name = "cart_id")
    @OneToOne
    private Cart cart;

    @OneToMany(mappedBy = "user")
    private List<UserCoupon> userCoupons = new ArrayList<>();

    public void setSeller(Seller seller) {
        seller.setUser(this);
        this.seller = seller;
    }

    public void setCart(Cart cart) {
        cart.setUser(this);
        this.cart = cart;
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
        String nickname, String phone, String password, Cart cart) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.password = password;
        setCart(cart);
    }
}
