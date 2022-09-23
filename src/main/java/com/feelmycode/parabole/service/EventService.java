package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.global.error.IdNotFoundException;
import com.feelmycode.parabole.repository.CouponRepository;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.ProductRepository;
import com.feelmycode.parabole.repository.SellerRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final SellerRepository sellerRepository;

    private final CouponRepository couponRepository;

    private final ProductRepository productRepository;

    /**
     *  이벤트 생성
     */
    @Transactional
    public Long Event(EventCreateRequestDto eventDto) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 엔티티 조회
        // TODO : Error Exception 수정
        Seller seller = sellerRepository.findById(eventDto.getSellerId()).orElseThrow(() -> new IdNotFoundException("해당하는 ID의 판매자가 없습니다."));

        List<Long> productIds = eventDto.getEventPrizeCreateRequestDtos().getProductIds();
        List<Long> couponIds  = eventDto.getEventPrizeCreateRequestDtos().getCouponIds();

        // 이벤트-경품정보 생성
        List<EventPrize> eventPrizeList = new ArrayList<>();

        if (CollectionUtils.isEmpty(productIds)) {
            for (Long productId : productIds) {
                Product product = productRepository.findById(productId).orElseThrow(() -> new IdNotFoundException("해당하는 ID의 상품이 없습니다."));
                EventPrize productPrize = new EventPrize("PRODUCT", eventDto.getEventPrizeCreateRequestDtos().getStock(), product);
            }
        }

        if (CollectionUtils.isEmpty(productIds)) {
            for (Long couponId : couponIds) {
                Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new IdNotFoundException("해당하는 ID의 쿠폰이 없습니다."));
                EventPrize couponPrize = new EventPrize("COUPON", eventDto.getEventPrizeCreateRequestDtos().getStock(), coupon);
                eventPrizeList.add(couponPrize);
            }
        }

        // 이벤트 생성
        Event event = Event.builder()
            .seller(seller)
            .createdBy(eventDto.getCreatedBy())
            .type(eventDto.getType())
            .title(eventDto.getTitle())
            .startAt(LocalDateTime.parse(eventDto.getStartAt(), formatter))
            .endAt(LocalDateTime.parse(eventDto.getEndAt(), formatter))
            .descript(eventDto.getDescript())
            .eventImage(eventDto.getEventImage())
            .eventPrizes(eventPrizeList)
            .build();

        // 이벤트 저장
        eventRepository.save(event);
        return event.getId();
    }

    /**
     * 이벤트 ID로 단건 조회
     */
    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow();
    }

    /**
     * Seller ID로 이벤트 목록 조회
     */
    public List<Event> getEventsBySellerId(Long sellerId) {
        return eventRepository.findAllBySellerIdAndIsDeleted(sellerId, false);
    }

    /**
     * 이벤트 전체 조회
     * (삭제된 이벤트 제외)
     */
    public List<Event> getEvents() {
        return eventRepository.findAllByIsDelted(false);
    }

    // TODO : 이벤트 수정

    /**
     * 이벤트 취소
     */
    @Transactional
    public void cancelEvent(Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        event.ifPresent(e -> {
            e.cancel();
            eventRepository.save(e);
        });
    }
}
