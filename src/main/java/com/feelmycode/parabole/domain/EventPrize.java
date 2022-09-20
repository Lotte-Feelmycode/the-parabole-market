package com.feelmycode.parabole.domain;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "event_prize")
@Getter
public class EventPrize {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_prize_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    private String prizeType; // [COUPON, PRODUCT]

    private Integer stock;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "prodcut_id")
    private Product product;

    public void setEvent(Event event) {
        this.event = event;
    }

    @Builder
    public EventPrize(String prizeType, Integer stock, Coupon coupon) {
        this.prizeType = prizeType;
        this.stock = stock;
        this.coupon = coupon;
    }

    @Builder
    public EventPrize(String prizeType, Integer stock, Product product) {
        this.prizeType = prizeType;
        this.stock = stock;
        this.product = product;
    }
}
