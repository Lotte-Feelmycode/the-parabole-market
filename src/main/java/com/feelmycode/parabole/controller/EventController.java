package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventListResponseDto;
import com.feelmycode.parabole.dto.EventSearchResponseDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.global.util.StringUtil;
import com.feelmycode.parabole.service.AwsS3Service;
import com.feelmycode.parabole.service.EventService;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class EventController {

    private final EventService eventService;
    private final AwsS3Service awsS3Service;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ParaboleResponse> createEvent(
        @RequestPart("eventDtos") @Valid EventCreateRequestDto eventDto, @RequestPart("images") List<MultipartFile> eventImages) {
        Long eventId = -1L;
        try {
            String bannerImg = awsS3Service.upload(eventImages.get(0));
            String detailImg = awsS3Service.upload(eventImages.get(1));
            eventDto.setEventImage(bannerImg, detailImg);

            eventId = eventService.createEvent(eventDto);
        } catch (Exception e) {
            throw new ParaboleException(HttpStatus.INTERNAL_SERVER_ERROR, "이벤트 등록 실패");
        }
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "이벤트 등록 성공", eventId);
    }

    // TODO: 셀러 스토어 정보 리턴값 추가
    @GetMapping("/{eventId}")
    public ResponseEntity<ParaboleResponse> getEvent(@PathVariable("eventId") Long eventId) {
        EventListResponseDto response = eventService.getEventByEventId(eventId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, eventId + "번 이벤트 조회 성공",
            response);
    }

    // TODO: 조회조건+정렬조건 추가
    @GetMapping
    public ResponseEntity<ParaboleResponse> getEvent() {
        List<EventListResponseDto> response = eventService.getEventListResponseDto(
            eventService.getEventsAllNotDeleted());
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 리스트 조회 성공", response);
    }

    @GetMapping("/list")
    public ResponseEntity<ParaboleResponse> getEventList(
        @RequestParam(required = false) String eventType,
        @RequestParam(required = false) String eventTitle,
        @RequestParam(required = false) Integer dateDiv,
        @RequestParam(required = false) LocalDateTime fromDateTime,
        @RequestParam(required = false) LocalDateTime toDateTime,
        @RequestParam(required = false) Integer eventStatus
    ) {
        Integer getDateDiv = StringUtil.controllerParamIsBlank(dateDiv + "") ? -1 : dateDiv;
        String getEventType = StringUtil.controllerParamIsBlank(eventType) ? "" : eventType;
        String getEventTitle = StringUtil.controllerParamIsBlank(eventTitle) ? "" : eventTitle;
        Integer getEventStatus = StringUtil.controllerParamIsBlank(eventStatus + "") ? -1 : eventStatus;

        List<EventSearchResponseDto> response = eventService.getEventsSearch(
            getEventType, getEventTitle, getDateDiv, fromDateTime, toDateTime, getEventStatus
        );
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 검색 리스트 조회 성공", response);
    }

    @GetMapping("/seller/{userId}")
    public ResponseEntity<ParaboleResponse> getEventByUserId(@PathVariable("userId") Long userId) {
        List<EventListResponseDto> response = eventService.getEventListResponseDto(
            eventService.getEventsBySellerId(userId));
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 리스트 조회 성공", response);
    }

    @GetMapping("/seller/scheduler")
    public ResponseEntity<ParaboleResponse> getEventScheduler() {
        List<EventSearchResponseDto> response = eventService.getEventsMonthAfter();
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 스케쥴러 조회 성공", response);
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
