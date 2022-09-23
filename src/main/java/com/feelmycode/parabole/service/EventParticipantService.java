package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventParticipant;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.EventApplyDto;
import com.feelmycode.parabole.repository.EventParticipantRepository;
import com.feelmycode.parabole.repository.EventPrizeRepository;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventParticipantService {

    private final EventParticipantRepository eventParticipantRepository;
    private final UserRepository userRepository;
    private final EventPrizeRepository eventPrizeRepository;
    private final EventRepository eventRepository;

    public boolean eventJoin(EventApplyDto eventApplyDto) {

        if (applyCheck(eventApplyDto)) {

            EventParticipant eventApply = eventApplyDto.toEntity(
                getUser(eventApplyDto.getUserId()),
                getEvent(eventApplyDto.getEventId()),
                getEventPrize(eventApplyDto.getPrizeId()),
                LocalDateTime.now());

            eventParticipantRepository.save(eventApply);
            return true;
        } else {
            return false;
        }
    }

    private boolean applyCheck(EventApplyDto eventApplyDto) {
        if (eventParticipantRepository.findByUserIdAndEventId(eventApplyDto.getUserId(),
            eventApplyDto.getEventId()).isPresent()) {
            return false;
        }
        return true;
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException());
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new IllegalArgumentException());
    }

    private EventPrize getEventPrize(Long eventPrizeId) {
        return eventPrizeRepository.findById(eventPrizeId)
            .orElseThrow(() -> new IllegalArgumentException());
    }
}
