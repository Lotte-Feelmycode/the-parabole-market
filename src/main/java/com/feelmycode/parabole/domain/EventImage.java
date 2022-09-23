package com.feelmycode.parabole.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class EventImage {

    @Column(name = "event_banner_img")
    private String eventBannerImg;

    @Column(name = "event_detail_img")
    private String eventDetailImg;

    public EventImage(String eventBannerImg, String eventDetailImg) {
        this.eventBannerImg = eventBannerImg;
        this.eventDetailImg = eventDetailImg;
    }
}

