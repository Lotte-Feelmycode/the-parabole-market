package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventListResponseDto;
import com.feelmycode.parabole.service.EventService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    @PostMapping()
    public ResponseEntity<?> createEvent(@RequestBody EventCreateRequestDto eventDto) {
        try {
            Long eventId = eventService.createEvent(eventDto);
            return ResponseEntity.ok(eventId);
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventListResponseDto> getEvent(@PathVariable("eventId") Long eventId) {
        Event eventEntity = eventService.getEventByEventId(eventId);
        EventListResponseDto response = EventListResponseDto.of(eventEntity);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<EventListResponseDto>> getEvent() {
        List<Event> eventEntities = eventService.getEventsAllNotDeleted();
        List<EventListResponseDto> response = eventEntities.stream()
            .map(EventListResponseDto::of)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
