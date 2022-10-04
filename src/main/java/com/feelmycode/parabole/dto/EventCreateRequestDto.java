package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.EventImage;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventCreateRequestDto {

    private Long userId;
    private String createdBy;
    private String type;
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String descript;
    private EventImage eventImage;
    private List<EventPrizeCreateRequestDto> eventPrizeCreateRequestDtos;

    public EventCreateRequestDto(Long userId, String createdBy, String type, String title,
        LocalDateTime startAt, LocalDateTime endAt, String descript, EventImage eventImage,
        List<EventPrizeCreateRequestDto> eventPrizeCreateRequestDtos) {
        this.userId = userId;
        this.createdBy = createdBy;
        this.type = type;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.descript = descript;
        this.eventImage = eventImage;
        this.eventPrizeCreateRequestDtos = eventPrizeCreateRequestDtos;
    }
}
