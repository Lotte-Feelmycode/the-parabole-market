package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.dto.EventDto;
import com.feelmycode.parabole.service.EventService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {

  private final EventService eventService;

  @GetMapping("/api/v1/events/{eventId}")
  public ResponseEntity<EventDto> getEvent(@PathVariable("eventId") Long eventId) {
    EventDto response = eventService.getEvent(eventId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/api/v1/events")
  public ResponseEntity<List<EventDto>> getEvent() {
    List<EventDto> response = eventService.getEvents();
    return ResponseEntity.ok(response);
  }
}
