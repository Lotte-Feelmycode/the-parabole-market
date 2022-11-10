package com.feelmycode.parabole.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.feelmycode.parabole.domain.EventImage;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventCreateRequestDto {

    @NotNull
    private String createdBy;

    @NotBlank(message = "이벤트 타입을 선택해주세요.")
    private String type;

    @NotBlank(message = "이벤트 제목을 입력해주세요.")
    private String title;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss")
    @NotNull
    private LocalDateTime startAt;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss")
    @NotNull
    private LocalDateTime endAt;

    @NotBlank(message = "이벤트 설명을 입력해주세요.")
    private String descript;
    private EventImage eventImage;
    private List<EventPrizeCreateRequestDto> eventPrizeCreateRequestDtos;

    public EventCreateRequestDto(String createdBy, String type, String title,
        LocalDateTime startAt, LocalDateTime endAt, String descript, EventImage eventImage,
        List<EventPrizeCreateRequestDto> eventPrizeCreateRequestDtos) {
        this.createdBy = createdBy;
        this.type = type;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.descript = descript;
        this.eventImage = eventImage;
        this.eventPrizeCreateRequestDtos = eventPrizeCreateRequestDtos;
    }
}
