package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.EventImage;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EventCreateRequestDto {

    private Long sellerId;
    private String createdBy;
    private String type;
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String descript;
    private EventImage eventImage;
    private List<EventPrizeCreateRequestDto> eventPrizeCreateRequestDtos;

}
