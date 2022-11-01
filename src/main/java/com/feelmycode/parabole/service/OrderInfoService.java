package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Order;
import com.feelmycode.parabole.domain.OrderInfo;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.dto.CouponResponseDto;
import com.feelmycode.parabole.dto.OrderInfoResponseDto;
import com.feelmycode.parabole.dto.OrderInfoSimpleDto;
import com.feelmycode.parabole.dto.OrderWithCouponResponseDto;
import com.feelmycode.parabole.dto.SellerDto;
import com.feelmycode.parabole.enumtype.OrderInfoState;
import com.feelmycode.parabole.repository.OrderInfoRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderInfoService {

    private final OrderInfoRepository orderInfoRepository;
    private final OrderService orderService;
    private final ProductService productService;
    private final SellerService sellerService;
    private final CouponService couponService;

    @Transactional
    public void saveOrderInfo(Long userId, OrderInfoSimpleDto orderInfoDto) {
        Order order = orderService.getOrder(userId);

        Product product = productService.getProduct(orderInfoDto.getProductId());
        SellerDto seller = sellerService.getSellerBySellerId(product.getSeller().getId());

        OrderInfo orderInfo = new OrderInfo(order,
            null,
            orderInfoDto.getProductId(),
            product.getName(),
            orderInfoDto.getProductCnt(),
            product.getPrice(),
            seller.getSellerId(),
            seller.getStoreName());

        // 각각의 주문상품에 대한 배달 여부 저장(default -1)
        orderInfo.setState(-1);

        orderInfoRepository.save(orderInfo);
    }

    public List<OrderInfoResponseDto> getOrderInfoListByUserId(List<Order> orderList) {
        List<OrderInfo> orderInfoList = new ArrayList<>();
        for (Order order : orderList) {
            orderInfoList.addAll(this.getOrderInfoListByOrderId(order.getId()));
        }
        return this.changeEntityToDto(orderInfoList);
    }

    public boolean isDeliveryComplete(Long userId) {
        List<OrderInfoResponseDto> orderInfoResponseDtoList = getOrderInfoListByUserId(userId);
        return orderInfoResponseDtoList.stream()
            .allMatch(dto -> OrderInfoState.returnValueByName(dto.getState()) > 5);
    }

    // TODO: 자동으로 상품에 적용할 수 있는 최대 쿠폰을 적용할 수 있게 하기
    public List<OrderInfoResponseDto> getOrderInfoListByUserId(Long userId) {
        Order order = orderService.getOrder(userId);
        List<OrderInfo> getOrderInfoList = getOrderInfoListByOrderId(order.getId());
        return changeEntityToDto(getOrderInfoList);
    }

    public List<OrderInfoResponseDto> getOrderInfoListBySeller(Long sellerId) {
        List<OrderInfo> getOrderInfoList = orderInfoRepository.findAllBySellerId(sellerId)
            .stream()
            .filter(state -> state.getState() != -1)
            .collect(Collectors.toList());
        return changeEntityToDto(getOrderInfoList);
    }

    public List<OrderInfo> getOrderInfoListByOrderId(Long orderId) {
        return orderInfoRepository.findAllByOrderId(orderId);
    }

    public List<OrderWithCouponResponseDto>[] getOrderInfoGroupBySellerIdOrderByIdDesc(
        Long userId) {

        Order order = orderService.getOrder(userId);

        List<OrderInfo> orderInfoList = getOrderInfoListByOrderId(order.getId())
            .stream()
            .sorted(Comparator.comparing(OrderInfo::getProductId).reversed())
            .toList();

        HashMap<Long, Integer> sellerIdMap = new HashMap<>();

        int idx = 0;
        for (OrderInfo orderInfo : orderInfoList) {
            Long sellerId = orderInfo.getSellerId();
            if (!sellerIdMap.containsKey(sellerId)) {
                sellerIdMap.put(sellerId, idx++);
            }
        }

        List<OrderInfoResponseDto>[] getOrderInfoList = new ArrayList[sellerIdMap.size() + 1];
        for (int i = 0; i <= sellerIdMap.size(); i++) {
            getOrderInfoList[i] = new ArrayList<>();
        }

        for (OrderInfo orderInfo : orderInfoList) {
            Long sellerId = orderInfo.getSellerId();
            getOrderInfoList[sellerIdMap.get(sellerId)].add(orderInfo.toDto());
        }

        HashMap<Long, CouponResponseDto> couponList = couponService.getCouponMapByUserId(userId);

        List<OrderWithCouponResponseDto>[] orderInfoWithCoupon = new ArrayList[sellerIdMap.size()
            + 1];

        for (int i = 0; i <= sellerIdMap.size(); i++) {
            orderInfoWithCoupon[i] = new ArrayList<>();
        }

        HashSet<Long> orderWithCouponSet = new HashSet<>();

        for (Long sellerId : sellerIdMap.keySet()) {
            if (orderWithCouponSet.add(sellerId)) {
                if (couponList.isEmpty()) {
                    orderInfoWithCoupon[sellerIdMap.get(sellerId)].add(
                        new OrderWithCouponResponseDto(sellerId,
                            sellerService.getSellerBySellerId(sellerId).getStoreName(),
                            getOrderInfoList[sellerIdMap.get(sellerId)],
                            new CouponResponseDto()));
                }
            } else {
                orderInfoWithCoupon[sellerIdMap.get(sellerId)].add(
                    new OrderWithCouponResponseDto(sellerId,
                        sellerService.getSellerBySellerId(sellerId).getStoreName(),
                        getOrderInfoList[sellerIdMap.get(sellerId)],
                        couponList.get(sellerId)));
            }
        }

        return orderInfoWithCoupon;
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
