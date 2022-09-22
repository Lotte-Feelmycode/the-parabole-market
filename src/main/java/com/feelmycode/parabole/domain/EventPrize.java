package com.feelmycode.parabole.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_prize")
@Getter
@NoArgsConstructor
public class EventPrize implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_prize_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @JsonBackReference
    private Event event;

    @Column(name = "event_prize_type")
    private String prizeType; // [COUPON, PRODUCT]

    @Column(name = "event_stock")
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

    public EventPrize(String prizeType, Integer stock, Coupon coupon) {
        this.prizeType = prizeType;
        this.stock = stock;
        this.coupon = coupon;
    }

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
