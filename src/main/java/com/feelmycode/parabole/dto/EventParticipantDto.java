package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventParticipant;
import com.feelmycode.parabole.domain.User;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class EventParticipantDto {

    private Long id;
    private UserDto user;
    private LocalDateTime eventTimeStartAt;

    public EventParticipantDto(EventParticipant eventParticipant) {
        id = eventParticipant.getId();
        user = new UserDto(eventParticipant.getUser());
        eventTimeStartAt = eventParticipant.getEventTimeStartAt();
    }

}
