package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.EventPrize;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;



public interface EventPrizeRepository extends JpaRepository<EventPrize,Long> {

    Optional<EventPrize> findById(Long eventPrizeId);
}
