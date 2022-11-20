package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.domain.CartItem;
import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.domain.OrderInfo;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.OrderInfoRequestDto;
import com.feelmycode.parabole.dto.OrderInfoRequestListDto;
import com.feelmycode.parabole.dto.OrderInfoResponseDto;
import com.feelmycode.parabole.dto.OrderRequestDto;
import com.feelmycode.parabole.enumtype.OrderInfoState;
import com.feelmycode.parabole.enumtype.OrderPayState;
import com.feelmycode.parabole.enumtype.OrderState;
import com.feelmycode.parabole.global.error.exception.NoDataException;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.CartItemRepository;
import com.feelmycode.parabole.repository.OrderInfoRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UpdateService {

    private final OrderInfoRepository orderInfoRepository;
    private final UserService userService;
    private final OrderInfoService orderInfoService;
    private final OrderService orderService;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductService productService;

    @Transactional
    public void updateOrderInfoState(Long userId, OrderInfoRequestDto orderInfoRequestDto) {
        try {
            OrderInfo getOrderInfo = orderInfoRepository.findById(orderInfoRequestDto.getOrderInfoId())
                .orElseThrow(() -> new NoDataException());

            getOrderInfo.setState(orderInfoRequestDto.getOrderInfoState());
            orderInfoRepository.save(getOrderInfo);

            // 모든 상품이 배송완료일 때 주문이 완료되었다고 처리
            Order order = orderService.getOrderByOrderId(getOrderInfo.getOrder().getId());

            User user = userService.getUser(order.getUser().getId());

            if (orderInfoService.isDeliveryComplete(user.getId())) {
                List<OrderInfo> getOrderInfoList = orderInfoService.getOrderInfoListByOrderId(order.getId());

                for (OrderInfo info : getOrderInfoList) {
                    info.setState(orderInfoRequestDto.getOrderInfoState());
                    Long productId = info.getProductId();
                    Product getProduct = productService.getProduct(productId);
                    getProduct.removeRemains(Long.valueOf(info.getProductCnt()));
                }

                this.updateOrderState(userId, new OrderRequestDto(
                    OrderPayState.returnNameByValue(order.getPayState()).getState()));
            }
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.UNAUTHORIZED, "주문정보를 수정하는 중 문제가 발생했습니다.");
        }
    }

    @Transactional
    public void updateOrderState(Long userId, OrderRequestDto orderUpdateRequestDto) {
        Order order = orderService.getOrder(userId);

        if(order == null) {
            throw new NoDataException();
        }

        if(order.getState() < 2) {
            order.setState(order.getState()+1);
            orderUpdateRequestDto.setOrderState(OrderState.PAY_COMPLETE);
        } else {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "이미 배송완료된 상품입니다.");
        }

        User getUser = userService.getUser(userId);
        orderUpdateRequestDto.setUserInfo(getUser.getUsername(), getUser.getEmail());

        if(orderUpdateRequestDto.getOrderPayState().equals(OrderPayState.BANK_TRANSFER) || orderUpdateRequestDto.getOrderPayState().equals(OrderPayState.WITHOUT_BANK)) {
            orderUpdateRequestDto.setOrderInfoState(OrderInfoState.BEFORE_PAY);
        } else {
            orderUpdateRequestDto.setOrderInfoState(OrderInfoState.DELIVERY);
        }


        order.saveDeliveryInfo(orderUpdateRequestDto);

        List<OrderInfo> orderInfoList = orderInfoService.getOrderInfoListByOrderId(order.getId());
        if(!orderUpdateRequestDto.getOrderInfoRequestList().isEmpty()) {
            orderInfoService.setCouponToOrderInfo(userId, orderUpdateRequestDto);
        }
        for(OrderInfo orderInfo : orderInfoList) {
            this.updateOrderInfoState(userId, new OrderInfoRequestDto(orderInfo.getId(),
                orderUpdateRequestDto.getOrderInfoState()));
        }

        // 주문이 완료 되었을 때 cart에 있는 아이템 삭제
        if (order.getState() == 0) {
            Cart getCart = cartService.getCart(userId);

            List<CartItem> cartItemList = cartItemRepository.findAllByCartId(getCart.getId());

            if (cartItemList == null) {
                return;
            }

            List<OrderInfoResponseDto> orderInfoResponseDtoList = orderInfoService.getOrderInfoListByUserId(userId);

            List<Long> cartIdList = cartItemList.stream()
                .filter(item -> orderInfoResponseDtoList.stream().anyMatch(
                    orderInfo -> item.getProduct().getId().equals(orderInfo.getProductId())))
                .map(CartItem::getId)
                .collect(Collectors.toList());

            if (cartIdList == null) {
                return;
            }

            cartItemRepository.deleteAllById(cartIdList);
        }
    }

}
