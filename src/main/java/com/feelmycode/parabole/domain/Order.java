package com.feelmycode.parabole.domain;

import com.sun.istack.NotNull;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderInfo> orderInfoList = new ArrayList<>();

    @NotNull
    @Column(name = "order_total")
    private long total;

    @NotNull
    @Column(name = "order_state")
    private int state;

    @NotNull
    @Column(name = "order_delivery_fee")
    private long deliveryFee;

    public void setDeleted() {
        this.isDeleted = false;
    }
    public void setState(int state) {
        this.state = state;
    }

    private void setOrderInfoList(List<OrderInfo> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }

    private void setTotal(List<OrderInfo> orderInfoList) {
        this.total = orderInfoList
            .stream()
            .mapToLong(OrderInfo::getProductPrice)
            .sum();
    }


    private void setDeliveryFee(long orderDeliveryFee) {
        this.deliveryFee = orderDeliveryFee;
    }

    // TODO: 주문상세 정보 list로 추가하기
    public Order(Long id, User user, int state, long deliveryFee) {
        this.id = id;
        this.user = user;
        this.setTotal(getOrderInfoList());
        this.state = state;
        this.deliveryFee = deliveryFee;
    }

    public Order(User user, int state, long deliveryFee) {
        this.user = user;
        this.setTotal(getOrderInfoList());
        this.state = state;
        this.deliveryFee = deliveryFee;
    }
    public Order setOrder(int state, List<OrderInfo> orderInfoList) {
        this.setState(state);
        this.setOrderInfoList(orderInfoList);
        this.setTotal(orderInfoList);
        return this;
    }

}
