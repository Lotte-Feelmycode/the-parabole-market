package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.domain.OrderInfo;
import com.feelmycode.parabole.repository.OrderInfoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderInfoService {

    private final OrderInfoRepository orderInfoRepository;
    private final OrderService orderService;

    @Transactional
    public void saveOrderInfo(OrderInfo orderInfo) {
        orderInfoRepository.save(orderInfo);
    }

    // TODO: 자동으로 상품에 적용할 수 있는 최대 쿠폰을 적용할 수 있게 하기
    public List<OrderInfo> getOrderInfoList(Long userId) {
        Order order = orderService.getOrderByUserId(userId);
        if(order == null) {
            orderService.createOrder(order);
        }
        return orderInfoRepository.findAllByOrderId(order.getId());
    }

}
