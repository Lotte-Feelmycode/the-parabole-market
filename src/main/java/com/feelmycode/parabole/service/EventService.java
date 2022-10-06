package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventListResponseDto;
import com.feelmycode.parabole.dto.EventPrizeCreateRequestDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.CouponRepository;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.ProductRepository;
import com.feelmycode.parabole.repository.SellerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final SellerService sellerService;

    private Seller getSeller(Long userId) {
        return sellerService.getSellerByUserId(userId);
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "해당하는 ID의 상품이 없습니다"));
    }

    private Coupon getCoupon(Long couponId) {
        return couponRepository.findById(couponId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "해당하는 ID의 쿠폰이 없습니다."));
    }

    public List<EventListResponseDto> getEventListResponseDto(List<Event> eventEntities) {
        return eventEntities.stream()
            .map(EventListResponseDto::of)
            .collect(Collectors.toList());
    }

    /**
     * 이벤트 생성
     */
    // TODO: JWT 처리 후 userId 처리
    // TODO: @Valid
    @Transactional
    public Long createEvent(EventCreateRequestDto eventDto) {

        // 엔티티 조회
        Seller seller = getSeller(eventDto.getUserId());

        // 이벤트-경품정보 생성
        List<EventPrize> eventPrizeList = new ArrayList<>();

        List<EventPrizeCreateRequestDto> eventPrizeParams = eventDto.getEventPrizeCreateRequestDtos();

        if (!CollectionUtils.isEmpty(eventPrizeParams)) {
            for (EventPrizeCreateRequestDto eventPrizeParam : eventPrizeParams) {
                String prizeType = eventPrizeParam.getType();
                Long id = eventPrizeParam.getId();
                if (prizeType.equals("PRODUCT")) {
                    eventPrizeList.add(
                        new EventPrize(prizeType, eventPrizeParam.getStock(), getProduct(id)));
                } else {
                    eventPrizeList.add(
                        new EventPrize(prizeType, eventPrizeParam.getStock(), getCoupon(id)));
                }
            }
        }

        // 이벤트 생성
        Event event = Event.builder()
            .seller(seller)
            .createdBy(eventDto.getCreatedBy()).type(eventDto.getType())
            .title(eventDto.getTitle()).startAt(eventDto.getStartAt()).endAt(eventDto.getEndAt())
            .descript(eventDto.getDescript()).eventImage(eventDto.getEventImage())
            .eventPrizes(eventPrizeList).build();

        // 이벤트 저장
        eventRepository.save(event);
        return event.getId();
    }

    /**
     * 이벤트 ID로 단건 조회
     */
    public Event getEventByEventId(Long eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "해당하는 ID의 이벤트가 없습니다"));
    }

    /**
     * Seller ID로 이벤트 목록 조회
     */
    public List<Event> getEventsBySellerId(Long userId) {
        Seller seller = sellerService.getSellerByUserId(userId);
        return eventRepository.findAllBySellerAndIsDeleted(seller, false);
    }

    /**
     * 이벤트 전체 조회 (삭제된 이벤트 제외)
     */
    public List<Event> getEventsAllNotDeleted() {
        return eventRepository.findAllByIsDeleted(false);
    }

    // TODO : 이벤트 수정

    /**
     * 이벤트 취소
     */
    @Transactional
    public void cancelEvent(Long eventId) {
        Event event = getEventByEventId(eventId);
        try {
            event.cancel();
            eventRepository.save(event);
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 등록 실패");
        }
    }
}
