package com.feelmycode.parabole.service;

import static org.junit.Assert.assertEquals;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventImage;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventDto;
import com.feelmycode.parabole.dto.EventListResponseDto;
import com.feelmycode.parabole.dto.EventPrizeCreateRequestDto;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.SellerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class eventServiceTest {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    EventService eventService;

    @Test
    public void 이벤트등록() throws Exception {
        //given
        List<Long> productIds = new ArrayList<>();
        productIds.add(1L);
        productIds.add(2L);

        EventPrizeCreateRequestDto eventPrizeCreateRequestDto = new EventPrizeCreateRequestDto("FCFS", 50, productIds, null);
        EventCreateRequestDto testEventDto = new EventCreateRequestDto(1L, "SELLER", "RAFFLE", "테스트등록이벤트", "2022-09-22 00:00:00", "2022-09-28 18:00:00","테스트 등록 이벤트 설명", new EventImage(), eventPrizeCreateRequestDto);

        //when
        Long eventId = eventService.Event(testEventDto);

        // then
        Event getEvent = eventRepository.findById(eventId).orElseThrow();

        assertEquals("이벤트 제목이 같아야 한다.", testEventDto.getTitle(), getEvent.getTitle());
        assertEquals("이벤트 상태 default는 0", Optional.of(0), Optional.of(getEvent.getStatus()));
    }
}
