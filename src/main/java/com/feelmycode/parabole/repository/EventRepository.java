package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Event;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Optional<Event> findByEventType(String eventType);
}
