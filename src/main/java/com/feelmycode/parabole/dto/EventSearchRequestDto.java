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

}
