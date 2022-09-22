package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventDto;
import com.feelmycode.parabole.dto.EventPrizeCreateRequestDto;
import com.feelmycode.parabole.global.error.IdNotFoundException;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.ProductRepository;
import com.feelmycode.parabole.repository.SellerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
     *  이벤트 생성
     */
    @Transactional
    public Long Event(EventCreateRequestDto eventCreateRequestDto, EventPrizeCreateRequestDto eventPrizeCreateRequestDto) {

        // 엔티티 조회
        Seller seller = sellerRepository.findById(eventCreateRequestDto.getSellerId()).orElseThrow(() -> new IdNotFoundException("해당하는 ID의 판매자가 없습니다."));
        System.out.println("seller Id : "+seller.getId());

        List<Long> productIds = eventPrizeCreateRequestDto.getProductIds();
        //List<Long> couponIds  = eventPrizeCreateRequestDto.getCouponIds();

        List<Product> products = new ArrayList<>();
        //List<Coupon> coupons = new ArrayList<>();

        for (Long productId : productIds) {
            Product product = productRepository.findById(productId).orElseThrow(() -> new IdNotFoundException("해당하는 ID의 상품이 없습니다."));
            products.add(product);
            System.out.println(product.getProductName()+"/n");
        }

        /*
        for (Long couponId : couponIds) {
            Coupon coupon = couponRepository.findById(coupontId).orElseThrow(() -> new IdNotFoundException("해당하는 ID의 쿠폰이 없습니다."));
            coupons.add(coupon);
        }
         */

        // 이벤트-경품정보 생성
        List<EventPrize> eventPrizeList = new ArrayList<>();

        for (Product product : products) {
            EventPrize productPrize = new EventPrize("PRODUCT", eventPrizeCreateRequestDto.getStock(), product);
            eventPrizeList.add(productPrize);
            System.out.println(productPrize.toString());
        }
        /*
        for (Coupon coupon : coupons) {
            EventPrize couponPrize = new EventPrize("COUPON", eventPrizeCreateRequestDto.getStock(), coupon);
            eventPrizeList.add(couponPrize);

        }
        */

        // 이벤트 생성
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

        // 이벤트 저장
        eventRepository.save(event);
        return event.getId();
    }

    /**
     * 이벤트 조회
     */
    public EventDto getEvent(Long eventId) {
        System.out.println("event service getevent : "+eventId);
        System.out.println(eventRepository.findById(eventId).map(EventDto::of).orElseThrow());
        EventDto dto = EventDto.of(eventRepository.findById(eventId).orElseThrow());
        return dto;
    }

    /**
     * 이벤트 전체 조회
     */
    public List<EventDto> getEvents(){
        return eventRepository.findAll().stream()
            .map(EventDto::of)
            .collect(Collectors.toList());
    }

}
