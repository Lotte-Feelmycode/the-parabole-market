package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventPrizeCreateRequestDto;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final SellerRepository sellerRepository;

    //private final CouponRepository couponRepository;

    private final ProductRepository productRepository;

    /**
     * 이벤트 생성
     *
     */
    //
    @Transactional
    public Long Event(EventCreateRequestDto eventCreateRequestDto, EventPrizeCreateRequestDto eventPrizeCreateRequestDto) {

        // 엔티티 조회
        Seller seller = sellerRepository.findById(eventCreateRequestDto.getSellerId()).orElseThrow();

        List<Long> productIds = eventPrizeCreateRequestDto.getProductIds();
        //List<Long> couponIds  = eventPrizeCreateRequestDto.getCouponIds();

        List<Product> products = new ArrayList<>();
        //List<Coupon> coupons = new ArrayList<>();

        for (Long productId : productIds) {
            Product product = productRepository.findById(productId).orElseThrow();
            products.add(product);
        }

        /*
        for (Long couponId : couponIds) {
            Coupon coupon = couponRepository.findById(coupontId).orElseThrow();
            coupons.add(coupon);
        }
         */

        // 이벤트-경품정보 생성
        List<EventPrize> eventPrizeList = new ArrayList<>();

        for (Product product : products) {
            EventPrize productPrize = new EventPrize("PRODUCT", eventPrizeCreateRequestDto.getStock(), product);
            eventPrizeList.add(productPrize);
        }
        /*
        for (Coupon coupon : coupons) {
            EventPrize couponPrize = new EventPrize("COUPON", eventPrizeCreateRequestDto.getStock(), coupon);
            eventPrizeList.add(couponPrize);

        }
        */

        Event event = Event.builder()
            .seller(seller)
            .eventBy(eventCreateRequestDto.getEventBy())
            .eventType(eventCreateRequestDto.getEventType())
            .eventTitle(eventCreateRequestDto.getEventTitle())
            .eventStartAt(eventCreateRequestDto.getEventStartAt())
            .eventEndAt(eventCreateRequestDto.getEventEndAt())
            .eventDescript(eventCreateRequestDto.getEventDescript())
            .eventImage(eventCreateRequestDto.getEventImage())
            .eventPrizes(eventPrizeList)
            .build();

        eventRepository.save(event);
        return event.getId();
    }



}
