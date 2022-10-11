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
    private Long total;

    @NotNull
    @Column(name = "order_user_name")
    private String userName;

    @NotNull
    @Column(name = "order_user_email")
    private String userEmail;

    @NotNull
    @Column(name = "order_user_phone")
    private String userPhone;

    @NotNull
    @Column(name = "order_receiver_name")
    private String receiverName;

    @NotNull
    @Column(name = "order_receiver_phone")
    private String receiverPhone;

    @NotNull
    @Column(name = "order_address_simple")
    private String addressSimple;

    @NotNull
    @Column(name = "order_address_detail")
    private String addressDetail;

    @NotNull
    @Column(name = "order_delivery_comment")
    private String deliveryComment;

    @NotNull
    @Column(name = "order_delivery_fee")
    private Long deliveryFee;

    public void setDeleted() {
        this.isDeleted = false;
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

    private void setDeliveryFee(Long orderDeliveryFee) {
        this.deliveryFee = orderDeliveryFee;
    }

    // TODO: 주문상세 정보 list로 추가하기

    public Order(User user, Long deliveryFee) {
        this.user = user;
        this.setTotal(getOrderInfoList());
        this.deliveryFee = deliveryFee;
    }

    public Order setOrder(List<OrderInfo> orderInfoList) {
        this.setOrderInfoList(orderInfoList);
        this.setTotal(orderInfoList);
        return this;
    }

}
