package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.EventImage;
import com.feelmycode.parabole.domain.EventPrize;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
