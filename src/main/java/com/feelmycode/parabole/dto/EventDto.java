package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventImage;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.Seller;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class EventDto {

    private Long id;
    private Seller seller;
    private String createdBy;
    private String type;
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer status;
    private String descript;
    private EventImage eventImage;
    private List<EventPrize> eventPrizes;

    public EventDto(Long id, Seller seller, String createdBy, String type, String title,
        LocalDateTime startAt, LocalDateTime endAt, Integer status, String descript,
        EventImage eventImage, List<EventPrize> eventPrizes) {
        this.id = id;
        this.seller = seller;
        this.createdBy = createdBy;
        this.type = type;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.descript = descript;
        this.eventImage = eventImage;
        this.eventPrizes = eventPrizes;
    }

    public EventDto(Long id, String title, LocalDateTime startAt, LocalDateTime endAt,
        Integer status, EventImage eventImage) {
        this.id = id;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.eventImage = eventImage;
    }

    static public EventDto of(Event event) {
        return EventDto.builder()
            .id(event.getId())
            .seller(event.getSeller())
            .createdBy(event.getCreatedBy())
            .type(event.getType())
            .title(event.getTitle())
            .startAt(event.getStartAt())
            .endAt(event.getEndAt())
            .status(event.getStatus())
            .descript(event.getDescript())
            .eventImage(event.getEventImage())
            .eventPrizes(event.getEventPrizes())
            .build();
    }

}
