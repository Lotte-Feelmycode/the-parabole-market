package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.enumtype.EventStatus;
import com.feelmycode.parabole.enumtype.EventType;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class EventSearchRequestDto {

    private EventType eventType;
    private String eventTitle;
    private Integer dateDiv;
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
    private EventStatus eventStatus;

    public EventSearchRequestDto(EventType eventType, String eventTitle, Integer dateDiv,
        LocalDateTime fromDateTime, LocalDateTime toDateTime, EventStatus eventStatus) {
        this.eventType = eventType;
        this.eventTitle = eventTitle;
        this.dateDiv = dateDiv;
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.eventStatus = eventStatus;
    }

}
