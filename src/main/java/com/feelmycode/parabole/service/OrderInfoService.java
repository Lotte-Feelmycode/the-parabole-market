package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.domain.OrderInfo;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.dto.OrderInfoResponseDto;
import com.feelmycode.parabole.dto.OrderInfoSimpleDto;
import com.feelmycode.parabole.enumtype.OrderState;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
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

    @Transactional
    public void saveOrderInfo(OrderInfoSimpleDto orderInfoDto) {
        Order order = orderService.getOrder(orderInfoDto.getUserId());
        if(order == null) {
            log.info("Order is null");
            order = orderService.createOrder(new Order(userService.getUser(orderInfoDto.getUserId()), DELIVERY_FEE));
        }
        log.info("Save Order Info. order: {}", order.toString());
        OrderInfo orderInfo = new OrderInfo(order,
                                    "KAKAO_PAY",
                                            orderInfoDto.getProductId(),
                                            orderInfoDto.getProductName(),
                                            orderInfoDto.getProductCnt(),
                                            orderInfoDto.getProductPrice());
        orderInfo.setState(1);
        orderInfoRepository.save(orderInfo);
    }

    @Transactional
    public void updateOrderState(Long orderInfoId, String orderState) {
        try {
            OrderInfo getOrderInfo = orderInfoRepository.findById(orderInfoId)
                .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "주문정보를 찾을 수 없습니다."));
            getOrderInfo.setState(OrderState.returnValueByName(orderState));
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.UNAUTHORIZED, "주문정보를 수정할 수 없습니다.");
        }
    }

    // TODO: 자동으로 상품에 적용할 수 있는 최대 쿠폰을 적용할 수 있게 하기
    public List<OrderInfoResponseDto> getOrderInfoList(Long userId) {
        Order order = orderService.getOrder(userId);
        List<OrderInfo> getOrderInfoList = orderInfoRepository.findAllByOrderId(order.getId());
        return changeEntityToDto(getOrderInfoList);
    }

    public List<OrderInfoResponseDto> getOrderInfoListBySeller(Long sellerId) {
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
