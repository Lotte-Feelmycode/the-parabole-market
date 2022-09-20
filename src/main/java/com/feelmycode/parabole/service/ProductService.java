package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.repository.ProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void saveOrUpdateProduct(Product product) {
        productRepository.save(product);
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException());
    }

}
