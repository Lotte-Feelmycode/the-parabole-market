package com.feelmycode.parabole.service;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.junit.Assert.assertEquals;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventImage;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventPrizeCreateRequestDto;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.ProductRepository;
import com.feelmycode.parabole.repository.SellerRepository;
import com.feelmycode.parabole.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class eventServiceTest {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    EventService eventService;


    @Test
    @Transactional
//    @Rollback(false)
    public void 이벤트등록v1() throws Exception {
        // given
        // user
        User user = userRepository.save(
            new User("eventTest@mail.com", "eventTest", "eventNickname", "010-3354-3342", "1234"));
        // seller
        Seller seller = new Seller("event Store Name", "3245918723");
        user.setSeller(seller);
        sellerRepository.save(seller);
        // product
        Product product = new Product(seller, 1, 50L, "CATEGORY", "thumb.img", "이벤트 테스트 상품",
            50000L);
        productRepository.save(product);

        //event prize
        List<EventPrize> prizes = new ArrayList<>();
        prizes.add(new EventPrize("PRODUCT", 50, product));
        product.removeRemains(50L);

        LocalDateTime startAt = LocalDateTime.parse("2022-10-31T00:00:00", ISO_LOCAL_DATE_TIME);
        LocalDateTime endAt = LocalDateTime.parse("2022-11-28T18:00:00", ISO_LOCAL_DATE_TIME);

        Event event = Event.builder()
            .seller(seller)
            .createdBy("SELLER").type("RAFFLE")
            .title("테스트등록이벤트").startAt(startAt).endAt(endAt)
            .descript("테스트 등록 이벤트 설명").eventImage(new EventImage())
            .eventPrizes(prizes).build();

        eventRepository.save(event);

        //when
        Long eventId =event.getId();

        // then
        Event getEvent = eventRepository.findById(eventId).orElseThrow();

        assertEquals("이벤트 제목이 같아야 한다.", event.getTitle(), getEvent.getTitle());
        assertEquals("이벤트 상태 default는 0", Optional.of(0), Optional.of(getEvent.getStatus()));
    }

    @Test
    @Transactional
//    @Rollback(false)
    public void 이벤트등록v2() throws Exception {
        // given
        User user = userRepository.save(
            new User("eventTest@mail.com", "eventTest", "eventNickname", "010-3354-3342", "1234"));
        // seller
        Seller seller = new Seller("event Store Name", "3245918723");
        user.setSeller(seller);
        sellerRepository.save(seller);

        LocalDateTime startAt = LocalDateTime.parse("2022-10-31T00:00:00", ISO_LOCAL_DATE_TIME);
        LocalDateTime endAt = LocalDateTime.parse("2022-11-28T18:00:00", ISO_LOCAL_DATE_TIME);

        // product
        Product product = new Product(seller, 1, 50L, "CATEGORY", "thumb.img", "이벤트 테스트 상품",
            50000L);
        productRepository.save(product);

        //event prize
        List<EventPrizeCreateRequestDto> prizes = new ArrayList<>();
        prizes.add(new EventPrizeCreateRequestDto(product.getId(), "PRODUCT", 40));
        EventCreateRequestDto requestDto = new EventCreateRequestDto(
            user.getId(), "SELLER", "RAFFLE", "이벤트 제목V2", startAt, endAt, "이벤트 설명 v2", new EventImage(), prizes
        );

        //when
        Long eventId = eventService.createEvent(requestDto);

        // then
        Event getEvent = eventRepository.findById(eventId).orElseThrow();

        assertEquals("이벤트 번호가 같아야 한다.", eventId, getEvent.getId());
        assertEquals("이벤트 상태 default는 0", Optional.of(0), Optional.of(getEvent.getStatus()));
        assertEquals("경품 등록 수량만큼 상품 재고 줄어야 한다.", 10L, Optional.of(product.getRemains()));

    }
}
