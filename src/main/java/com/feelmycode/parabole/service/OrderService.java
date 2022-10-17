package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.domain.CartItem;
import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.OrderDeliveryUpdateRequestDto;
import com.feelmycode.parabole.dto.OrderUpdateRequestDto;
import com.feelmycode.parabole.enumtype.OrderState;
import com.feelmycode.parabole.repository.CartItemRepository;
import com.feelmycode.parabole.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
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
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    @Transactional
    public Order createOrder(Long userId, Order order) {
        Order getOrder =  orderRepository.save(order);
        return getOrder;
    }

    @Transactional
    public Order getOrder(Long userId) {
        Order getOrder = null;
        getOrder = this.getOrderByUserId(userId);
        return getOrder;
    }

    @Transactional
    public void updateDeliveryInfo(OrderDeliveryUpdateRequestDto deliveryDto) {
        Order order = this.getOrder(deliveryDto.getUserId());
        order.saveDeliveryInfo(deliveryDto);
    }

    public boolean isOrderEmpty(Long userId) {
        Order order = this.getOrder(userId);
        if (order == null)
            return true;
        if (order.getState() < 0) {
            this.deleteOrder(order.getId());
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public Order getOrderByUserId(Long userId) {
        return orderRepository.findTop1ByUserIdOrderByIdDesc(userId);
    }

    public void updateOrderState(OrderUpdateRequestDto orderUpdateRequestDto) {

        Order order = this.getOrder(orderUpdateRequestDto.getUserId());
        order.setState(orderUpdateRequestDto.getOrderState());

        // 주문이 완료 되었을 때 cart에 있는 아이템 삭제
        if(OrderState.returnValueByName(orderUpdateRequestDto.getOrderState()).getValue() != -1) {
            Cart getCart = cartService.getCart(orderUpdateRequestDto.getUserId());

            List<CartItem> cartItemList = cartItemRepository.findAllByCartId(getCart.getId());

            List<Long> cartIdList = cartItemList.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());

            cartItemRepository.deleteAllByIdIn(cartIdList);
        }
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
