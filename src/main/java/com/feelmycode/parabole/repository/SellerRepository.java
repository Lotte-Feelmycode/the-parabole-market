//package com.feelmycode.parabole.repository;
//
//import com.feelmycode.parabole.domain.Seller;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//public interface SellerRepository extends JpaRepository<Seller, Long> {
//
//    @Query("SELECT s FROM Seller s WHERE s.id =:sellerId")
//    Seller findSellerBySellerId(@Param("sellerId") Long sellerId);
//
//}
