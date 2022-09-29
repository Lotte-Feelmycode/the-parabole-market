package com.feelmycode.parabole.domain;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "sellers")
public class Seller extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @NotNull
    @OneToOne
    private User user;

    @Column(name = "seller_store_name", length = 200)
    @NotNull
    private String storeName;

    @Column(name = "seller_registration_no")
    @NotNull
    private String registrationNo;


    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Coupon> coupons = new ArrayList<>();


    public Seller(String storeName, String registrationNo) {
        this.storeName = storeName;
        this.registrationNo = registrationNo;
    }

    public Seller(User user, String storeName, String registrationNo) {
        this.user = user;
        this.storeName = storeName;
        this.registrationNo = registrationNo;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
