package com.feelmycode.parabole.domain;

import static javax.persistence.FetchType.LAZY;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events", uniqueConstraints = {
    @UniqueConstraint(name = "event_seller_unique", columnNames = {"event_id", "seller_id"})})
@Getter
@NoArgsConstructor

public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonBackReference
    private Seller seller;

    @Column(name = "event_by")
    private String eventBy; // [ADMIN, SELLER]

    @Column(name = "event_type")
    private String eventType; // [FCFS, REAFFLE]

    @Column(name = "event_title")
    private String eventTitle;

    @Column(name = "event_start_at")
    private String eventStartAt;

    @Column(name = "event_end_at")
    private String eventEndAt;

    @Column(name = "event_status")
    private Integer eventStatus; // 0:시작전, 1:진행중, 2:종료

    @Column(name = "event_descript")
    private String eventDescript;

    @Embedded
    private EventImage eventImage;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = LAZY)
    @JsonManagedReference
    private List<EventPrize> eventPrizes = new ArrayList<>();

    public void setSeller(Seller seller) {
        this.seller = seller;
        seller.getEvents().add(this);
    }

    public void addEventPrize(EventPrize eventPrize) {
        eventPrizes.add(eventPrize);
        eventPrize.setEvent(this);
    }

    @Builder
    public Event(Seller seller, String eventBy, String eventType, String eventTitle,
        String eventStartAt, String eventEndAt, String eventDescript, EventImage eventImage,
        List<EventPrize> eventPrizes) {
        this.seller = seller;
        this.eventBy = eventBy;
        this.eventType = eventType;
        this.eventTitle = eventTitle;
        this.eventStartAt = eventStartAt;
        this.eventEndAt = eventEndAt;
        this.eventStatus = 0; // 시작전
        this.eventDescript = eventDescript;
        this.eventImage = eventImage;
        //this.eventPrizes = eventPrizes;
        for (EventPrize eventPrize : eventPrizes) {
            addEventPrize(eventPrize);
        }
    }

    // TODO : 이벤트 수정, 삭제 및 재고관리 로직 추가

}
