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
    private String bannerImg;

    @Column(name = "event_detail_img")
    private String detailImg;

    public EventImage(String bannerImg, String detailImg) {
        this.bannerImg = bannerImg;
        this.detailImg = detailImg;
    }
}
