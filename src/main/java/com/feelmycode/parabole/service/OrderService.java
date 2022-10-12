package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.domain.OrderInfo;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.OrderRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public Long createOrder(Order order) {
        Order getOrder = orderRepository.save(order);
        return getOrder.getId();
    }

    @Transactional
    public Order updateOrderState(Long userId, Long orderId) {
        log.info("접근 성공");
        Order getOrder = checkAuthentication(userId, orderId);
        getOrder.setOrder(getOrder.getOrderInfoList());
        return getOrder;
    }

    public Order checkAuthentication(Long userId, Long orderId) {
        Order getOrder = null;
        getOrder = this.getOrder(orderId);
        if(getOrder == null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "주문을 찾을 수 없습니다");
        }
        if (getOrder.getUser().getId() != userId) {
            throw new ParaboleException(HttpStatus.UNAUTHORIZED, "주문에 접근할 수 없습니다.");
        }
        return getOrder;
    }
    @Transactional
    public void deleteOrder(Long userId, Long orderId) {
        Order getOrder = checkAuthentication(userId, orderId);
        getOrder.setDeleted();
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.BAD_REQUEST, "주문된 상품이 없습니다."));
    }

    public Order getOrderByUserId(Long userId) {
        return orderRepository.findTopByUserIdOrderByIdDesc(userId);
    }

    // TODO: 셀러 별로 상품을 가져오기
    // TODO: 제일 할인율이 높은 쿠폰을 각각의 셀러에게 적용하기
    public void categorizeOrder(List<OrderInfo> list) {
        HashMap<Long, Integer> saveSellerIdByIdx = new HashMap<>();
        List<Long> sellerInfo = new ArrayList();

        int idx = 0;

        for(OrderInfo orderInfo : list) {
            Order order = orderInfo.getOrder();
//            Seller seller = order.getUser().getSeller();
//            if(saveSellerIdByIdx.containsKey(seller.getId())) {
//                continue;
//            } else {
//                saveSellerIdByIdx.put(seller.getId(), idx++);
//                sellerInfo.add(seller.getId());
//            }
        }

        List<Product>[] productInfo = new ArrayList[idx];
//        Page<CouponAvailianceResponseDto> couponInfo = couponService.getUserCouponList()

        for(OrderInfo orderInfo : list) {
            Order order = orderInfo.getOrder();
//            Seller seller = order.getUser().getSeller();
//            productInfo[saveSellerIdByIdx.get(seller.getId())].add(productService.getProduct(orderInfo.getProductId()));
        }
    }

}
