package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Coupon;
import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventListResponseDto;
import com.feelmycode.parabole.dto.EventPrizeCreateRequestDto;
import com.feelmycode.parabole.dto.EventSearchRequestDto;
import com.feelmycode.parabole.dto.EventSearchResponseDto;
import com.feelmycode.parabole.enumtype.EventStatus;
import com.feelmycode.parabole.enumtype.EventType;
import com.feelmycode.parabole.enumtype.PrizeType;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.global.util.StringUtil;
import com.feelmycode.parabole.repository.CouponRepository;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.ProductRepository;
import com.feelmycode.parabole.repository.SellerRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
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
            .map(EventListResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * 이벤트 생성
     */
    // TODO: JWT 처리 후 userId 처리
    // TODO: @Valid
    @Transactional
    public Long createEvent(Long userId, EventCreateRequestDto eventDto) {

        // 엔티티 조회
        Seller seller = getSeller(userId);

        // 이벤트-경품정보 생성
        List<EventPrize> eventPrizeList = new ArrayList<>();

        List<EventPrizeCreateRequestDto> eventPrizeParams = eventDto.getEventPrizeCreateRequestDtos();

        if (!CollectionUtils.isEmpty(eventPrizeParams)) {
            for (EventPrizeCreateRequestDto eventPrizeParam : eventPrizeParams) {
                String prizeType = eventPrizeParam.getType();
                Long id = eventPrizeParam.getId();
                if (prizeType.equals(PrizeType.PRODUCT.getCode())) {
                    Product product = productRepository.findById(eventPrizeParam.getId())
                        .orElseThrow(
                            () -> new ParaboleException(HttpStatus.NOT_FOUND, "해당하는 상품이 없습니다")
                        );

                    product.removeRemains(Long.valueOf(eventPrizeParam.getStock()));
                    eventPrizeList.add(
                        new EventPrize(prizeType, eventPrizeParam.getStock(), getProduct(id)));
                } else {
                    Coupon coupon = couponRepository.findById(eventPrizeParam.getId()).orElseThrow(
                        () -> new ParaboleException(HttpStatus.NOT_FOUND, "해당하는 쿠폰 없습니다")
                    );
                    eventPrizeList.add(
                        new EventPrize(prizeType, eventPrizeParam.getStock(), getCoupon(id)));
                    coupon.setCouponForEvent(eventPrizeParam.getStock());
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
    public EventListResponseDto getEventByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "해당하는 ID의 이벤트가 없습니다"));
        return new EventListResponseDto(event);
    }

    /**
     * Seller ID로 이벤트 목록 조회
     */
    public List<Event> getEventsBySellerId(Long userId) {
        Seller seller = sellerService.getSellerByUserId(userId);
        return eventRepository.findAllBySellerAndIsDeleted(seller, false);
    }

    /**
     * 검색 조건으로 이벤트 목록 조회 (경품 목록 제외)
     */
    public List<EventSearchResponseDto> getEventsSearch(
        String eventType, String eventTitle, Integer dateDiv, LocalDateTime fromDateTime,
        LocalDateTime toDateTime, Integer eventStatus) {

        List<Event> eventList = null;
        List<String> types =
            eventType.equals("") ? Arrays.asList("RAFFLE", "FCFS") : Arrays.asList(eventType);
        List<Integer> statuses =
            eventStatus < 0 ? Arrays.asList(0, 1, 2) : Arrays.asList(eventStatus);


        System.out.println("eventStatus" + eventStatus);
        System.out.println("statues "+ statuses);
        if (dateDiv > -1) {
            eventList = dateDiv < 1
                ? eventRepository.findAllByStartAtBetweenAndIsDeleted(fromDateTime, toDateTime,
                false)
                : eventRepository.findAllByEndAtBetweenAndIsDeleted(fromDateTime, toDateTime,
                    false);
        } else if (eventTitle.equals("")) {
            eventList = eventRepository.findAllByTypeInAndStatusInAndIsDeleted(
                types, statuses, false);
        } else {
            eventList = eventRepository.findAllByTypeInAndStatusInAndTitleContainingAndIsDeleted(
                types, statuses, eventTitle, false);
        }

        return eventList.stream()
            .map(EventSearchResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * 이벤트 전체 조회 (삭제된 이벤트 제외)
     */
    public List<Event> getEventsAllNotDeleted() {
        return eventRepository.findAllByIsDeleted(false);
    }

    /**
     * 이벤트 조회 (셀러 오피스 이벤트 목록 조회용)
     */
    public List<EventSearchResponseDto> getEventsMonthAfter() {
        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime monthAfterDate = nowDate.plusMonths(1L);
        return eventRepository.findAllByStartAtBetweenAndIsDeleted(nowDate,
                monthAfterDate, false)
            .stream()
            .filter(event -> event.getType().equals("FCFS"))
            .sorted(Comparator.comparing(Event::getStartAt))
            .map(EventSearchResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * 이벤트 취소
     */
    @Transactional
    public void cancelEvent(Long eventId) {
        log.info("eventId : {}번 이벤트 삭제", eventId);

        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "해당 이벤트가 존재하지 않습니다."));

        try {
            event.cancel();
            for (EventPrize eventPrize : event.getEventPrizes()) {
                if (eventPrize.getPrizeType().equals(PrizeType.PRODUCT.getCode())) {
                    log.info("취소하려는 이벤트의 경품 정보가 없습니다");
                    Product product = productRepository.findById(eventPrize.getProduct().getId())
                        .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND,
                            "취소하려는 이벤트의 경품 정보가 없습니다"));
                    product.addRemains(Long.valueOf(eventPrize.getStock()));
                } else {
                    log.info("취소하려는 이벤트의 쿠폰 정보가 없습니다");

                    Coupon coupon = couponRepository.findById(eventPrize.getCoupon().getId())
                        .orElseThrow(
                            () -> new ParaboleException(HttpStatus.NOT_FOUND,
                                "취소하려는 이벤트 경품 쿠폰 정보가 없습니다.")
                        );
                    coupon.cancelCouponEvent(eventPrize.getStock());

                }
            }
            eventRepository.save(event);


        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
