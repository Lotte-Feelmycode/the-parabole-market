package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.dto.EventApplyDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.EventParticipantService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
