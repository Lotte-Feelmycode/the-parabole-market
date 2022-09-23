package com.feelmycode.parabole.domain;

import static javax.persistence.FetchType.LAZY;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_prize")
@Getter
@NoArgsConstructor
public class EventPrize {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_prize_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonBackReference
    private Event event;

    private String prizeType; // [COUPON, PRODUCT]

    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    @JsonBackReference
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "prodcut_id")
    @JsonBackReference
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

    @Override
    public String toString() {
        return "EventPrize{" +
            "id=" + id +
            ", event=" + event +
            ", prizeType='" + prizeType + '\'' +
            ", stock=" + stock +
            ", coupon=" + coupon +
            ", product=" + product +
            '}';
    }
}

