package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllBySellerIdAndIsDeletedFalse(Long sellerId, Pageable pageable);
    Page<Product> findAllByCategoryAndIsDeletedFalse(String category, Pageable pageable);
    Page<Product> findAllBySellerIdAndCategoryAndIsDeletedFalse(Long sellerId, String category, Pageable pageable);
    Page<Product> findAllByNameContainingAndIsDeletedFalse(String name, Pageable pageable);
    Page<Product> findAllByNameContainingAndCategoryAndIsDeletedFalse(String name, String category, Pageable pageable);
    Product findByProductName(String name);
}
