package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductList(Long sellerId, String sellerName, String productName, String category, Pageable pageable) {

        // TODO : sellerName 조회 후 sellerId로 조회

        if(!sellerId.equals(0L)) {
            if (category.equals("")) {
                return productRepository.findAllBySellerId(sellerId, pageable);
            } else {
                return productRepository.findAllBySellerIdAndProductCategory(sellerId, category,
                    pageable);
            }
        } else if(!productName.equals("")) {
            if (category.equals("")) {
                return productRepository.findAllByProductNameContaining(productName, pageable);
            } else {
                return productRepository.findAllByProductNameContainingAndProductCategory(productName, category, pageable);
            }
        }

        if(category.equals("")) {
            return productRepository.findAll(pageable);
        } else {
            return productRepository.findAllByProductCategory(category, pageable);
        }

    }

}
