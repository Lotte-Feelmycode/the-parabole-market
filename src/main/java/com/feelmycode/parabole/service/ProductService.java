package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.ProductListGetResponseDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional(readOnly = true)

@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerService sellerService;

    @Transactional
    public Long saveProduct(Long userId, Product product) {
        sellerService.getSellerByUserId(userId);

        product.setSeller(sellerService.getSellerByUserId(userId));
        productRepository.save(product);

        return product.getId();
    }

    @Transactional
    public Long updateProduct(Long userId, Product product) {
        sellerService.getSellerByUserId(userId);
        Product getProduct = this.getProduct(product.getId());
        getProduct.setProduct(product);
        productRepository.save(getProduct);
        return product.getId();
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.BAD_REQUEST, "상품이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Page<ProductListGetResponseDto> getProductList(Long sellerId, String storeName, String productName, String category, Pageable pageable) {

        if(!storeName.equals("")) {
            Seller seller = sellerService.getSellerByStoreName(storeName);
            sellerId = seller.getId();
        }

        Page<Product> data;
        if(!sellerId.equals(0L)) {
            if (category.equals("")) {
                data = productRepository.findAllBySellerId(sellerId, pageable);
            } else {
                data = productRepository.findAllBySellerIdAndCategory(sellerId, category,
                    pageable);
            }
        } else if(!productName.equals("")) {
            if (category.equals("")) {
                data = productRepository.findAllByNameContaining(productName, pageable);
            } else {
                data = productRepository.findAllByNameContainingAndCategory(productName, category, pageable);
            }
        } else if(category.equals("")) {
            data = productRepository.findAll(pageable);
        } else {
            data = productRepository.findAllByCategory(category, pageable);
        }

        return data.map(ProductListGetResponseDto::new);
    }

}
