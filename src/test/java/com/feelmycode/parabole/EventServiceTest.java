package com.feelmycode.parabole;


import static org.junit.Assert.assertEquals;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventImage;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventDto;
import com.feelmycode.parabole.dto.EventPrizeCreateRequestDto;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.SellerRepository;
import com.feelmycode.parabole.service.EventService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventServiceTest {

    @Autowired EventRepository eventRepository;
    @Autowired SellerRepository sellerRepository;

    @Autowired EventService eventService;

    @Test
    public void 이벤트등록() throws Exception {
        List<Long> productIds = new ArrayList<>();
        productIds.add(1L);
        productIds.add(2L);

        productIds.stream().forEach(System.out::println);

        EventCreateRequestDto eventCreateRequestDto = new EventCreateRequestDto(1L, "SELLER", "RAFFLE", "테스트등록", "20220922", "20220925", 0, "테스트설명", new EventImage());
        EventPrizeCreateRequestDto eventPrizeCreateRequestDto = new EventPrizeCreateRequestDto("PRODUCT", 10, productIds, null);


        Long eventId = eventService.Event(eventCreateRequestDto, eventPrizeCreateRequestDto);
        System.out.println("eventId생성 : " + eventId);

        EventDto eventDto = eventService.getEvent(eventId);
        Event result = eventRepository.findById(eventId).orElseThrow();
        assertEquals("이벤트 제목", eventDto.getEventTitle(), result.getEventTitle());
    }

    @Test
    public void eventTest() throws Exception {

        //엔티티 조회
        //Seller seller = sellerRepository.findById(1L).orElseThrow();

        //product Id로 product 오브젝트 조회하는 부분 생략
        List<Product> products = new ArrayList<>();
        Product product = new Product(new Seller(), 1, 50L, "국밥", "https://img.url", "순대국밥", 15000L);
        Product pizza = new Product(new Seller(), 1, 50L, "피자", "https://img.url", "피자", 15000L);

        //이벤트-경품정보 생성
        List<EventPrize> eventPrizes = new ArrayList<>();
        for (Product p : products) {
            EventPrize productPrize = new EventPrize("PRODUCT", 100, p);
            eventPrizes.add(productPrize);
        }

        // 이벤트 이미지 생성
        EventImage eventImage = new EventImage("eventBannerImg.png", "eventDetailImg.png");

        Event newEvent = Event.builder()
            .eventBy("SELLER")
            .eventType("RAFFLE")
            .eventTitle("이벤트등록테스트")
            .eventStartAt("20220921")
            .eventEndAt("20220925")
            .eventDescript("이벤트설명")
            .eventImage(eventImage)
            .eventPrizes(eventPrizes)
            .build();

        eventRepository.save(newEvent);
        System.out.println(newEvent);



        EventDto eventDto = eventService.getEvent(1L);
        System.out.println(eventDto);

        Event event = eventRepository.findById(1L).orElseThrow();
        System.out.println(event);
    }
}
