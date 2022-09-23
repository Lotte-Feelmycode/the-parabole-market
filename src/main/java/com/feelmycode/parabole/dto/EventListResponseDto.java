package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventImage;
import com.feelmycode.parabole.domain.EventPrize;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EventListResponseDto {

    private Long id;
    private Long sellerId;
    private String createdBy;
    private String type;
    private String title;
    private String startAt;
    private String endAt;
    private Integer status;
    private String descript;
    private EventImage eventImage;
    private List<EventPrize> eventPrizes = new ArrayList<>();

    static public EventListResponseDto of(Event event) {
        return EventListResponseDto.builder()
            .id(event.getId())
            .sellerId(event.getSeller().getId())
            .createdBy(event.getCreatedBy())
            .type(event.getType())
            .title(event.getTitle())
            .startAt(event.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .endAt(event.getEndAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .status(event.getStatus())
            .descript(event.getDescript())
            .eventImage(event.getEventImage())
            .eventPrizes(event.getEventPrizes())
            .build();
    }

}
