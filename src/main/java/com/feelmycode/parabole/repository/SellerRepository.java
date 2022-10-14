package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

    Seller findByRegistrationNo(String registrationNo);
    Seller findByStoreName(String storeName);
    Seller findBySellerId(Long sellerId);

}
