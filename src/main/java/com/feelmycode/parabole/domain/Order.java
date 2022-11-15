package com.feelmycode.parabole.domain;

import com.feelmycode.parabole.dto.OrderRequestDto;
import com.feelmycode.parabole.enumtype.OrderState;
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
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
@Slf4j
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

    // TODO: enum으로 처리하기
    @NotNull
    @Column(name = "order_state")
    private Integer state;

    @NotNull
    @Column(name = "order_pay_state")
    private Integer payState;

    private void setTotal(List<OrderInfo> orderInfoList) {
        this.total = orderInfoList
            .stream()
            .mapToLong(OrderInfo::getProductPrice)
            .sum();
    }

    public void setState(String state) {
        this.state = OrderState.returnValueByName(state).getValue();
    }

    public void setState(Integer value) {
        this.state = value;
    }

    public Order(User user, Long deliveryFee) {
        this.user = user;
        this.setTotal(getOrderInfoList());
        this.deliveryFee = deliveryFee;
        this.addressSimple = "";
        this.addressDetail = "";
        this.deliveryComment = "";
        this.state = -1;
        this.payState = -1;
        this.setState(-1);
    }

    public Order saveDeliveryInfo(OrderRequestDto deliveryDto) {
        this.userName = deliveryDto.getUserName();
        this.userEmail = deliveryDto.getUserEmail();
        this.userPhone = deliveryDto.getUserPhone();
        this.receiverName = deliveryDto.getReceiverName();
        this.receiverPhone = deliveryDto.getReceiverPhone();
        this.addressSimple = deliveryDto.getAddressSimple();
        this.addressDetail = deliveryDto.getAddressDetail();
        this.deliveryComment = deliveryDto.getDeliveryComment();
        this.payState = deliveryDto.getOrderPayState();
        return this;
    }

}
