package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventImage;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.Seller;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Getter
public class EventDto {

  private Long id;
  private Seller seller;
  private String eventBy;
  private String eventType;
  private String eventTitle;
  private String eventStartAt;
  private String eventEndAt;
  private Integer eventStatus;
  private String eventDescript;
  private EventImage eventImage;
  private List<EventPrize> eventPrizes = new ArrayList<>();

  static public EventDto of(Event event) {
    return EventDto.builder()
        .id(event.getId())
        .seller(event.getSeller())
        .eventBy(event.getEventBy())
        .eventType(event.getEventType())
        .eventTitle(event.getEventTitle())
        .eventStartAt(event.getEventStartAt())
        .eventEndAt(event.getEventEndAt())
        .eventStatus(event.getEventStatus())
        .eventDescript(event.getEventDescript())
        .eventImage(event.getEventImage())
        .eventPrizes(event.getEventPrizes())
        .build();
  }

}
