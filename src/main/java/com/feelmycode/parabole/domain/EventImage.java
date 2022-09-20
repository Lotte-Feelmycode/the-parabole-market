package com.feelmycode.parabole.domain;

import javax.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class EventImage {

    private String eventBannerImg;

    private String eventDetailImg;

    protected EventImage() {

    }

    public EventImage(String eventBannerImg, String eventDetailImg) {
        this.eventBannerImg = eventBannerImg;
        this.eventDetailImg = eventDetailImg;
    }
}
