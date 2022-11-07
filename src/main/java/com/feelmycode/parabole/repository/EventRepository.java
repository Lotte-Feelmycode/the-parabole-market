package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Event;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.enumtype.EventStatus;
import com.feelmycode.parabole.enumtype.EventType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByIsDeleted(boolean isDeleted);
    List<Event> findAllBySellerAndIsDeleted(Seller seller, boolean isDeleted);
    List<Event> findAllBySellerIdOrderByStartAtAsc(Long SellerId);
    List<Event> findAllByStartAtBetweenAndIsDeleted(LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isDeleted);
    List<Event> findAllByEndAtBetweenAndIsDeleted(LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isDeleted);
    List<Event> findAllByTypeAndStatusAndTitleContainingAndIsDeleted(String eventType, Integer eventStauts, String eventTitle, boolean isDeleted);
}
