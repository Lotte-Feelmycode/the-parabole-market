package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.EventParticipant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class EventParticipantDto {

    private Long id;
    private UserDto user;
    private List<EventPrizeDto> eventPrizes;
    private LocalDateTime eventTimeStartAt;

    public EventParticipantDto(EventParticipant eventParticipant) {
        id = eventParticipant.getId();
        user = new UserDto(eventParticipant.getUser());
        eventPrizes = eventParticipant.getEvent().getEventPrizes().stream().map(EventPrizeDto::new).toList();
        eventTimeStartAt = eventParticipant.getEventTimeStartAt();
    }

}
