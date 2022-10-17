package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.domain.OrderInfo;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.dto.OrderInfoResponseDto;
import com.feelmycode.parabole.dto.OrderInfoSimpleDto;
import com.feelmycode.parabole.enumtype.OrderInfoState;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.CartItemRepository;
import com.feelmycode.parabole.repository.OrderInfoRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderInfoService {

    private static final Long DELIVERY_FEE = 0L;

    private final OrderInfoRepository orderInfoRepository;
    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;


    @Transactional
    public void saveOrderInfo(Long userId, OrderInfoSimpleDto orderInfoDto) {
        Order order = orderService.getOrder(userId);
        if (order == null) {
            order = orderService.createOrder(new Order(userService.getUser(userId), DELIVERY_FEE));
        }
        else if (order.getState() == -1) {
            orderService.deleteOrder(order.getId());
            order = orderService.createOrder(new Order(userService.getUser(userId), DELIVERY_FEE));
        }
        Product product = productService.getProduct(orderInfoDto.getProductId());
        OrderInfo orderInfo = new OrderInfo(order,
            orderInfoDto.getProductId(),
            product.getName(),
            orderInfoDto.getProductCnt(),
            product.getPrice());
        orderInfo.setState(-1);
        orderInfoRepository.save(orderInfo);
    }

    @Transactional
    public void updateOrderInfoState(Long orderInfoId, String orderState) {
        try {
            OrderInfo getOrderInfo = orderInfoRepository.findById(orderInfoId)
                .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "주문정보를 찾을 수 없습니다."));

            getOrderInfo.setState(OrderInfoState.returnValueByName(orderState));
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.UNAUTHORIZED, "주문정보를 수정하는 중 문제가 발생했습니다.");
        }
    }

    // TODO: 자동으로 상품에 적용할 수 있는 최대 쿠폰을 적용할 수 있게 하기
    public List<OrderInfoResponseDto> getOrderInfoList(Long userId) {
        Order order = orderService.getOrder(userId);
        List<OrderInfo> getOrderInfoList = orderInfoRepository.findAllByOrderId(order.getId());
        return changeEntityToDto(getOrderInfoList);
    }

    public List<OrderInfoResponseDto> getOrderInfoListBySeller(Long sellerId) {
        log.info("Service sellerId: {}", sellerId);
        List<OrderInfo> getOrderInfoList = orderInfoRepository.findAllBySellerId(sellerId);
        return changeEntityToDto(getOrderInfoList);
    }

    public List<OrderInfoResponseDto> changeEntityToDto(List<OrderInfo> orderInfoList) {
        List<OrderInfoResponseDto> orderInfoResponseDtoList = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfoList) {
            OrderInfoResponseDto responseDto = orderInfo.toDto();
            Product getProduct = productService.getProduct(orderInfo.getProductId());
            responseDto.setProductThumbnailImg(getProduct.getThumbnailImg());
            responseDto.setProductRemain(getProduct.getRemains());
            orderInfoResponseDtoList.add(responseDto);
        }
        return orderInfoResponseDtoList;
    }

}
