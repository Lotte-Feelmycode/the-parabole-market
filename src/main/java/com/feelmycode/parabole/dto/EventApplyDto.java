package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventParticipant;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.User;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EventApplyDto {

    @NotNull
    private Long userId;
    @NotNull
    private Long eventId;
    @NotNull
    private Long eventPrizeId;
    private LocalDateTime participantAt;

    public EventApplyDto(Long userId, Long eventId, Long eventPrizeId, LocalDateTime participantAt) {
        this.userId = userId;
        this.eventId = eventId;
        this.eventPrizeId = eventPrizeId;
        this.participantAt = participantAt;
    }

    public EventParticipant toEntity(User user, Event event, EventPrize prize,
        LocalDateTime eventTimeStartAt) {
        return new EventParticipant(user, event, prize, eventTimeStartAt);
    }
}
