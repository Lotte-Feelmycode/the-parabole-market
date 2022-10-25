package com.feelmycode.parabole.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestEventApplyCheckDto {
    @NotNull
    private Long eventId;
    @NotNull
    private Long userId;

    RequestEventApplyCheckDto(Long eventId,Long userId){
        this.eventId=eventId;
        this.userId=userId;
    }

}
