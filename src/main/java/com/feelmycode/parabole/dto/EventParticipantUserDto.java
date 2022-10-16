package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventParticipant;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventParticipantUserDto {
    private Long userId;
    private Long eventId;

    private LocalDateTime eventTimeStartAt;
    private String eventTitle;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer status;
    private String eventImg;

    public EventParticipantUserDto(EventParticipant eventParticipant){
        Event event=eventParticipant.getEvent();
        this.userId=eventParticipant.getUser().getId();
        this.eventId = event.getId();
        this.eventTimeStartAt=eventParticipant.getEventTimeStartAt();
        this.eventTitle = event.getTitle();
        this.startAt = event.getStartAt();
        this.endAt = event.getEndAt();
        this.status = event.getStatus();
        this.eventImg=event.getEventImage().getEventBannerImg();
    }
}
