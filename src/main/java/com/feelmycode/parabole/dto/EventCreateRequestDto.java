package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.EventImage;
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
    private String startAt;
    private String endAt;
    private String descript;
    private EventImage eventImage;
    private EventPrizeCreateRequestDto eventPrizeCreateRequestDtos;

}
