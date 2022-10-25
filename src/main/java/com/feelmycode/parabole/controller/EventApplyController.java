package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.dto.EventApplyDto;
import com.feelmycode.parabole.dto.EventParticipantDto;
import com.feelmycode.parabole.dto.EventParticipantUserDto;
import com.feelmycode.parabole.dto.RequestEventApplyCheckDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.EventParticipantService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
public class EventApplyController {

    private final EventParticipantService eventParticipantService;

    @PostMapping("/participant")
    public ResponseEntity<ParaboleResponse> insertEventApply(@RequestBody @Valid EventApplyDto dto) {
        eventParticipantService.eventJoin(dto);
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "응모가 완료 되었습니다");
    }

    @PostMapping("/participant/check")
    public ResponseEntity<ParaboleResponse> eventApplyCheck(@RequestBody RequestEventApplyCheckDto dto) {
        if (!eventParticipantService.eventApplyCheck(dto)) {
            return ParaboleResponse.CommonResponse(HttpStatus.ALREADY_REPORTED, true,
                dto.getEventId() + "번 이벤트에 이미 응모하였습니다", false);
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true,
            dto.getEventId() + "번 이벤트에 응모한적이 없습니다", true);
    }

    @GetMapping("/seller/participant/{eventId}")
    public ResponseEntity<ParaboleResponse> getEventParticipants(@PathVariable Long eventId) {
        List<EventParticipantDto> response = eventParticipantService.getEventParticipants(eventId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이벤트 응모 리스트 조회 성공", response);
    }

    @GetMapping("/user/participant/{userId}")
    public ResponseEntity<ParaboleResponse> getUserEventParticipants(@PathVariable Long userId) {
        List<EventParticipantUserDto> response = eventParticipantService.getEventParticipantUser(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "유저 이벤트 응모 리스트 조회 성공", response);
    }

}
