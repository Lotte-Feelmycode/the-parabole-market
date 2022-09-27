package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Seller findByUserId(Long userId);
    Seller findByRegistrationNo(String registrationNo);

}
