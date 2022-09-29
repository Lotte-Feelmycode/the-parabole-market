package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
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

    private final static String URL = "localhost:8080/api/v1/product/detail/";
    private final ProductRepository productRepository;
    private final SellerService sellerService;

    @Transactional
    public Long saveProduct(Long userId, Product product) {
        sellerService.getSellerByUserId(userId);

        product.setSeller(sellerService.getSellerByUserId(userId));

        Long productId = productRepository.countById(userId)+1;
        product.setUrl(URL+productId);

        productRepository.save(product);

        return productId;
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
    public Page<Product> getProductList(Long sellerId, String storeName, String productName, String category, Pageable pageable) {

        if(sellerId == 0L || sellerId == null) {
            Seller seller = sellerService.getSellerByStoreName(storeName);
            sellerId = seller.getId();
        }

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
