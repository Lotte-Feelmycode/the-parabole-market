package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllBySellerId(Long sellerId, Pageable pageable);
    Page<Product> findAllByCategory(String category, Pageable pageable);
    Page<Product> findAllBySellerIdAndCategory(Long sellerId, String category, Pageable pageable);
    Page<Product> findAllByNameContaining(String name, Pageable pageable);
    Page<Product> findAllByNameContainingAndCategory(String name, String category, Pageable pageable);
    Long countById(Long productId);

}
