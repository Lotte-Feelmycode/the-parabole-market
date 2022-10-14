package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventImage;
import com.feelmycode.parabole.domain.EventPrize;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventListResponseDto {

    private Long id;
    private Long sellerId;
    private String createdBy;
    private String type;
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer status;
    private String descript;
    private EventImage eventImage;
    private List<EventPrizeDto> eventPrizes;

    public EventListResponseDto(Long id, Long sellerId, String createdBy, String type, String title,
        LocalDateTime startAt, LocalDateTime endAt, Integer status, String descript,
        EventImage eventImage, List<EventPrizeDto> eventPrizes) {
        this.id = id;
        this.sellerId = sellerId;
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
    public EventListResponseDto(Event event){
        this.id =event.getId();
        this.sellerId = event.getSeller().getId();
        this.createdBy = event.getCreatedBy();
        this.type = event.getType();
        this.title = event.getTitle();
        this.startAt = event.getStartAt();
        this.endAt = event.getEndAt();
        this.status = event.getStatus();
        this.descript = event.getDescript();
        this.eventImage = event.getEventImage();
        this.eventPrizes = event.getEventPrizes().stream().map(EventPrizeDto::new).toList();
    }

}
