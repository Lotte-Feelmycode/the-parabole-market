package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.ProductDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    List<ProductDetail> findAllByProductId(Long productId);
}
