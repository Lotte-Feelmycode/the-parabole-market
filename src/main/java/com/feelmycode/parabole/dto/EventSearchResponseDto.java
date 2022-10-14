package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventImage;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventSearchResponseDto {

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

    public EventSearchResponseDto(Event event) {
        id = event.getId();
        sellerId = event.getSeller().getId();
        createdBy = event.getCreatedBy();
        type = event.getType();
        title = event.getTitle();
        startAt = event.getStartAt();
        endAt = event.getEndAt();
        status = event.getStatus();
        descript = event.getDescript();
        eventImage = event.getEventImage();
    }
}
