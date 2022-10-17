package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.domain.OrderInfo;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.OrderInfoRequestDto;
import com.feelmycode.parabole.dto.OrderInfoResponseDto;
import com.feelmycode.parabole.dto.OrderInfoSimpleDto;
import com.feelmycode.parabole.dto.OrderUpdateRequestDto;
import com.feelmycode.parabole.dto.SellerDto;
import com.feelmycode.parabole.enumtype.OrderInfoState;
import com.feelmycode.parabole.enumtype.OrderState;
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
    private final SellerService sellerService;

    @Transactional
    public void saveOrderInfo(Long userId, OrderInfoSimpleDto orderInfoDto) {
        Order order = orderService.getOrder(userId);

//        if (order == null) {
//            order = orderService.createOrder(new Order(userService.getUser(userId), DELIVERY_FEE));
//        }
//        // 이미 저장된 주문(취소했던 주문)이 있을 경우
//        else if (order.getState() < 0) {
//            orderService.deleteOrder(order.getId());
//            order = orderService.createOrder(new Order(userService.getUser(userId), DELIVERY_FEE));
//        }

        Product product = productService.getProduct(orderInfoDto.getProductId());
        SellerDto seller = sellerService.getSellerBySellerId(orderInfoDto.getSellerId());

        OrderInfo orderInfo = new OrderInfo(order,
            orderInfoDto.getProductId(),
            product.getName(),
            orderInfoDto.getProductCnt(),
            product.getPrice(),
            seller.getSellerId(),
            seller.getStoreName());

        // 각각의 주문상품에 대한 배달 여부
        orderInfo.setState(-1);

        orderInfoRepository.save(orderInfo);
    }

    @Transactional
    public void updateOrderInfoState(OrderInfoRequestDto orderInfoRequestDto) {
        try {
            OrderInfo getOrderInfo = orderInfoRepository.findById(orderInfoRequestDto.getOrderInfoId())
                .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "주문정보를 찾을 수 없습니다."));

            getOrderInfo.setState(OrderInfoState.returnValueByName(orderInfoRequestDto.getOrderState()));

            // 모든 상품이 배송완료일 때 주문이 완료되었다고 처리
            if(isDeliveryComplete(orderInfoRequestDto.getUserId())) {
                orderService.updateOrderState(new OrderUpdateRequestDto(
                    orderInfoRequestDto.getUserId(), OrderState.returnNameByValue(1)));
            }
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.UNAUTHORIZED, "주문정보를 수정하는 중 문제가 발생했습니다.");
        }
    }

    public boolean isDeliveryComplete(Long userId) {
        List<OrderInfoResponseDto> orderInfoResponseDtoList = getOrderInfoList(userId);
        return orderInfoResponseDtoList.stream()
            .allMatch(dto -> OrderInfoState.returnValueByName(dto.getState()) > 5);
    }

    // TODO: 자동으로 상품에 적용할 수 있는 최대 쿠폰을 적용할 수 있게 하기
    public List<OrderInfoResponseDto> getOrderInfoList(Long userId) {
        Order order = orderService.getOrder(userId);
        List<OrderInfo> getOrderInfoList = getOrderInfo(order.getId());
        return changeEntityToDto(getOrderInfoList);
    }

    public List<OrderInfoResponseDto> getOrderInfoListBySeller(Long sellerId) {
        log.info("Service sellerId: {}", sellerId);
        List<OrderInfo> getOrderInfoList = orderInfoRepository.findAllBySellerId(sellerId);
        return changeEntityToDto(getOrderInfoList);
    }

    public List<OrderInfo> getOrderInfo(Long orderId) {
        return orderInfoRepository.findAllByOrderId(orderId);
    }

    @Transactional
    public void updateOrderInfoState(Long userId, String state) {
        Order order = orderService.getOrder(userId);
        List<OrderInfo> getOrderInfoList = getOrderInfo(order.getId());
        for(OrderInfo info : getOrderInfoList) {
            info.setState(OrderInfoState.returnValueByName(state));
        }
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
