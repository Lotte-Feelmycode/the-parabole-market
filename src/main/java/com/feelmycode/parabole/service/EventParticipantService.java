package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventParticipant;
import com.feelmycode.parabole.domain.EventPrize;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.EventApplyDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.EventParticipantRepository;
import com.feelmycode.parabole.repository.EventPrizeRepository;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventParticipantService {

    private final EventParticipantRepository eventParticipantRepository;
    private final UserRepository userRepository;
    private final EventPrizeRepository eventPrizeRepository;
    private final EventRepository eventRepository;

    public void eventJoin(EventApplyDto eventApplyDto) {
        applyCheck(eventApplyDto);
        EventParticipant eventApply = eventApplyDto.toEntity(
            getUser(eventApplyDto.getUserId()),
            getEvent(eventApplyDto.getEventId()),
            getEventPrize(eventApplyDto.getEventPrizeId()),
            LocalDateTime.now());

        eventParticipantRepository.save(eventApply);
    }

    private void applyCheck(EventApplyDto eventApplyDto) {
        EventParticipant eventParticipant = eventParticipantRepository.findByUserIdAndEventId(
            eventApplyDto.getUserId(), eventApplyDto.getEventId());
        if (eventParticipant != null) {
            throw new ParaboleException(HttpStatus.ALREADY_REPORTED, "이미 응모 완료 되었습니다");
        }
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "찾을수 없는 회원입니다"));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "존재하지 않는 이벤트 입니다"));
    }

    private EventPrize getEventPrize(Long eventPrizeId) {
        return eventPrizeRepository.findById(eventPrizeId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다"));
    }
}
