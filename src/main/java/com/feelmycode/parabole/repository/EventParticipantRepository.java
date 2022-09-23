package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.EventParticipant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {

    Optional<EventParticipant> findByUserIdAndEventId(Long userId, Long eventId);
}
