package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.EventImage;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventCreateRequestDto {
    private Long sellerId;
    private String eventBy;
    private String eventType;
    private String eventTitle;
    private String eventStartAt;
    private String eventEndAt;
    private Integer eventStatus;
    private String eventDescript;
    private EventImage eventImage;

}
