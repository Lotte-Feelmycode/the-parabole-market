package com.feelmycode.parabole.domain;

import static javax.persistence.FetchType.LAZY;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.time.LocalDateTime;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events", uniqueConstraints = {
    @UniqueConstraint(name = "event_seller_unique", columnNames = {"event_id", "seller_id"})})
@Getter
@NoArgsConstructor
public class Event extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "seller_id")
//    @JsonBackReference
//    private Seller seller;
    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "created_by")
    private String createdBy; // [ADMIN, SELLER]

    @Column(name = "event_type")
    private String type; // [FCFS, REAFFLE]

    @Column(name = "event_title")
    private String title;

    @Column(name = "event_start_at")
    private LocalDateTime startAt;

    @Column(name = "event_end_at")
    private LocalDateTime endAt;

    @Column(name = "event_status")
    private Integer status; // 0:시작전, 1:진행중, 2:종료

    @Column(name = "event_descript")
    private String descript;

    @Embedded
    private EventImage eventImage;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = LAZY)
    @JsonManagedReference
    private List<EventPrize> eventPrizes = new ArrayList<>();

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setDeleted() {
        this.isDeleted = false;
    }

//    public void setSeller(Seller seller) {
//        this.seller = seller;
//        seller.getEvents().add(this);
//    }

    public void addEventPrize(EventPrize eventPrize) {
        eventPrizes.add(eventPrize);
        eventPrize.setEvent(this);
    }

    @Builder
    public Event(/*Seller seller*/Long sellerId, String createdBy, String type, String title,
        LocalDateTime startAt, LocalDateTime endAt, String descript, EventImage eventImage,
        List<EventPrize> eventPrizes) {
        //this.seller = seller;
        this.sellerId = sellerId;
        this.createdBy = createdBy;
        this.type = type;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = 0; // 시작전
        this.descript = descript;
        this.eventImage = eventImage;
        for (EventPrize eventPrize : eventPrizes) {
            addEventPrize(eventPrize);
        }
    }

    // TODO : 이벤트 수정 및 재고관리 로직 추가
    /**
     * 이벤트 취소(삭제)
     */
    public void cancel() {
        if(status!=0 || (LocalDateTime.now()).isAfter(startAt)) {
            throw new IllegalStateException("이미 시작된 이벤트는 취소가 불가능합니다.");
        }

        setDeleted();
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            //", seller=" + seller +
            ", sellerId=" + sellerId + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", type='" + type + '\'' +
            ", title='" + title + '\'' +
            ", startAt='" + startAt + '\'' +
            ", endAt='" + endAt + '\'' +
            ", status=" + status +
            ", descript='" + descript + '\'' +
            ", eventImage=" + eventImage +
            ", eventPrizes=" + eventPrizes +
            '}';
    }
}
