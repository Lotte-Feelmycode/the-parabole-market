package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional(readOnly = true)

@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Long saveProduct(Product product) {
        productRepository.save(product);
        return product.getId();
    }

    @Transactional
    public Long updateProduct(Product product) {
        Product getProduct = this.getProduct(product.getId());
        getProduct.setProduct(product);
        productRepository.save(getProduct);
        return product.getId();
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException());
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductList(Long sellerId, String sellerName, String productName, String category, Pageable pageable) {

        // TODO : sellerName 조회 후 sellerId로 조회

        if(!sellerId.equals(0L)) {
            if (category.equals("")) {
                return productRepository.findAllBySellerId(sellerId, pageable);
            } else {
                return productRepository.findAllBySellerIdAndCategory(sellerId, category,
                    pageable);
            }
        } else if(!productName.equals("")) {
            if (category.equals("")) {
                return productRepository.findAllByNameContaining(productName, pageable);
            } else {
                return productRepository.findAllByNameContainingAndCategory(productName, category, pageable);
            }
        }

        if(category.equals("")) {
            return productRepository.findAll(pageable);
        } else {
            return productRepository.findAllByCategory(category, pageable);
        }
    }

}