package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Event;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByIsDeleted(boolean isDeleted);
    List<Event> findAllBySellerIdAndIsDeleted(Long sellerId, boolean isDeleted);
    List<Event> findAllBySellerIdOrderByStartAtAsc(Long SellerId);

}
