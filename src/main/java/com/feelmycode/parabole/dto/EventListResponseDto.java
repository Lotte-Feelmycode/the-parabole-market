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
    private List<EventPrize> eventPrizes;

    public EventListResponseDto(Long id, Long sellerId, String createdBy, String type, String title,
        LocalDateTime startAt, LocalDateTime endAt, Integer status, String descript,
        EventImage eventImage, List<EventPrize> eventPrizes) {
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

    static public EventListResponseDto of(Event event) {
        return EventListResponseDto.builder()
            .id(event.getId())
            .sellerId(event.getSeller().getId())
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
