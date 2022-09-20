package com.feelmycode.parabole.domain;

import static javax.persistence.FetchType.LAZY;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events", uniqueConstraints = {@UniqueConstraint(
    name = "event_seller_unique",
    columnNames = {"event_id", "seller_id"}
)})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long Id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private String eventBy; // [ADMIN, SELLER]

    private String eventType; // [FCFS, REAFFLE]

    private String eventTitle;

    private String eventStartAt;

    private String eventEndAt;

    private Integer eventStatus; // 0:시작전, 1:진행중, 2:종료

    private String eventDescript;

    @Embedded
    private EventImage eventImage;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventPrize> eventPrizes = new ArrayList<>();

    //==연관관계 메소드==//
    public void setSeller(Seller seller) {
        this.seller = seller;
        seller.getEvents().add(this);
    }

    public void addEventPrize(EventPrize eventPrize) {
        eventPrizes.add(eventPrize);
        eventPrize.setEvent(this);
    }



}
