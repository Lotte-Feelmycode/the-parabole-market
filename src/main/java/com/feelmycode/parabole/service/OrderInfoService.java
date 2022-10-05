package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.domain.OrderInfo;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.UserCoupon;
import com.feelmycode.parabole.dto.OrderInfoListDto;
import com.feelmycode.parabole.dto.OrderInfoResponseDto;
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

    private final OrderInfoRepository orderInfoRepository;
    private final OrderService orderService;
    private final SellerService sellerService;
    private final ProductService productService;

    @Transactional
    public void saveOrderInfo(OrderInfoListDto orderInfoListDto) {
        Order order = orderService.getOrderByUserId(orderInfoListDto.getUserId());
        log.info("Save Order Info. order: {}", order.toString());
        if(order == null) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "주문정보를 찾을 수 없습니다.");
        }
        OrderInfo orderInfo = new OrderInfo(order, new UserCoupon(), orderInfoListDto.getProductId(), orderInfoListDto.getProductName(), orderInfoListDto.getProductCnt(), orderInfoListDto.getProductPrice(), orderInfoListDto.getProductDiscountPrice());
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

    public List<OrderInfoResponseDto> getOrderInfoListBySeller(Long userId) {
        List<OrderInfoResponseDto> orderInfoList = new ArrayList<>();
        List<OrderInfo> getOrderInfoList = orderInfoRepository.findAll();

        for(OrderInfo orderInfo : getOrderInfoList) {
            Product getProduct = productService.getProduct(orderInfo.getProductId());
            if(getProduct.getSeller().getId() == userId) {
                OrderInfoResponseDto responseDto = orderInfo.toDto();
                responseDto.setProductThumbnailImg(getProduct.getThumbnailImg());
                responseDto.setProductRemain(getProduct.getRemains());
                orderInfoList.add(responseDto);
            }
        }
        return orderInfoList;
    }

}