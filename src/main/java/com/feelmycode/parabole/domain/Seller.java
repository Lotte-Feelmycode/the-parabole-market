package com.feelmycode.parabole.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

    // TODO : 미완성 Entity 정식 버전 필요

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

    public Seller(String storeName, String registrationNo) {
        this.storeName = storeName;
        this.registrationNo = registrationNo;
    }

    public void setUser(User user) {
        user.setSeller(this);     // owner
        this.user = user;
    }

}
