package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllBySellerId(Long sellerId, Pageable pageable);
    Page<Product> findAllByProductCategory(String productCategory, Pageable pageable);
    Page<Product> findAllBySellerIdAndProductCategory(Long sellerId, String productCategory, Pageable pageable);
    Page<Product> findAllByProductNameContaining(String productName, Pageable pageable);
    Page<Product> findAllByProductNameContainingAndProductCategory(String productName, String productCategory, Pageable pageable);
}
