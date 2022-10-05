package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventListResponseDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.service.EventService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ParaboleResponse> createEvent(@RequestBody @Valid EventCreateRequestDto eventDto) {
        Long eventId = -1L;
        try {
            eventId = eventService.createEvent(eventDto);
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 등록 실패");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "이벤트 등록 성공", eventId);
    }

    // TODO: 셀러 스토어 정보 리턴값 추가
    @GetMapping("/{eventId}")
    public ResponseEntity<ParaboleResponse> getEvent(@PathVariable("eventId") Long eventId) {
        EventListResponseDto response = EventListResponseDto.of(eventService.getEventByEventId(eventId));
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, eventId+"번 이벤트 조회 성공", response);
    }

    // TODO: 조회조건+정렬조건 추가
    @GetMapping
    public ResponseEntity<ParaboleResponse> getEvent() {
        List<EventListResponseDto> response = eventService.getEventListResponseDto(eventService.getEventsAllNotDeleted());
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 리스트 조회 성공", response);
    }

    @GetMapping("/seller/{userId}")
    public ResponseEntity<ParaboleResponse> getEventByUserId(@PathVariable("userId") Long userId) {
        List<EventListResponseDto> response = eventService.getEventListResponseDto(eventService.getEventsBySellerId(userId));
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 리스트 조회 성공", response);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ParaboleResponse> cancelEvent(@PathVariable("eventId") Long eventId) {
        try {
            eventService.cancelEvent(eventId);
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 취소 실패");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 취소 성공");
    }
}
