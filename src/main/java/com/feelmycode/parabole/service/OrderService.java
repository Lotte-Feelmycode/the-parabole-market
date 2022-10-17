package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private static final Long DELIVERY_FEE = 0L;

    private final OrderRepository orderRepository;
    private final UserService userService;

    @Transactional
    public Order createOrder(Order order) {
        Order getOrder = orderRepository.save(order);
        return getOrder;
    }

    public Order getOrder(Long userId) {
        Order getOrder = null;
        getOrder = this.getOrderByUserId(userId);
        if(getOrder == null) {
            createOrder(new Order(userService.getUser(userId), DELIVERY_FEE));
        }
        return getOrder;
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order getOrder = getOrder(orderId);
        getOrder.setDeleted();
    }

    public Order getOrderByUserId(Long userId) {
        return orderRepository.findTopByUserIdOrderByIdDesc(userId);
    }

    // TODO: 셀러 별로 상품을 가져오기
    // TODO: 제일 할인율이 높은 쿠폰을 각각의 셀러에게 적용하기

//    public void categorizeOrder(List<OrderInfoResponseDto> list) {
//        HashMap<Long, Integer> saveSellerIdByIdx = new HashMap<>();
//        List<Long> sellerInfo = new ArrayList();
//
//        int idx = 0;

        // HashMap을 사용해서 sellerInfo에 셀러별로 저장
//        for(OrderInfoResponseDto orderInfo : list) {
//            Order order = orderInfoService.getOrderInfoList();
//            Seller seller = order.getUser().getSeller();
//            if(saveSellerIdByIdx.containsKey(seller.getId())) {
//                continue;
//            } else {
//                saveSellerIdByIdx.put(seller.getId(), idx++);
//                sellerInfo.add(seller.getId());
//            }
//        }

        // sellerInfo에 저장한 값을 토대로 셀러별 상품주문 리스트 저장
//        List<Product>[] productInfo = new ArrayList[idx];
//        Page<CouponAvailianceResponseDto> couponInfo = couponService.getUserCouponList()
//
//        for(OrderInfo orderInfo : list) {
//            Order order = orderInfo.getOrder();
//            Seller seller = order.getUser().getSeller();
//            productInfo[saveSellerIdByIdx.get(seller.getId())].add(productService.getProduct(orderInfo.getProductId()));
//        }
//
//        for(List<Product> productList : productInfo) {
//            for(Product p : productList) {
//                System.out.println(p);
//            }
//            System.out.println();
//        }
//    }

}
