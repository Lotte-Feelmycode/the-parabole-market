package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.ProductDetailDto;
import com.feelmycode.parabole.dto.ProductDetailListResponseDto;
import com.feelmycode.parabole.dto.ProductDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.ProductRepository;
import java.util.List;
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
    private final ProductDetailService productDetailService;
    private final SellerService sellerService;

    @Transactional
    public Long saveProduct(Long userId, ProductDto dto) {
        sellerService.getSellerByUserId(userId);

        Product product = dto.dtoToEntity();
        product.setSeller(sellerService.getSellerByUserId(userId));

        return productRepository.save(product).getId();
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

    public ProductDetailListResponseDto getProductDetail(Long productId) {
        Product getProduct = getProduct(productId);
        List<ProductDetailDto> productDetailList = productDetailService.getProductDetailList(productId).stream().map(ProductDetailDto::new).toList();
        return new ProductDetailListResponseDto(new ProductDto(getProduct), productDetailList, getProduct.getSeller().getStoreName());
    }

    public Page<ProductDto> getProductList(Long sellerId, String storeName, String productName, String category, Pageable pageable) {

        if(!storeName.equals("")) {
            Seller seller = sellerService.getSellerByStoreName(storeName);
            sellerId = seller.getId();
        }

        Page<Product> data;
        if(!sellerId.equals(0L)) {
            if (category.equals("")) {
                data = productRepository.findAllBySellerIdAndIsDeletedFalse(sellerId, pageable);
            } else {
                data = productRepository.findAllBySellerIdAndCategoryAndIsDeletedFalse(sellerId, category,
                    pageable);
            }
        } else if(!productName.equals("")) {
            if (category.equals("")) {
                data = productRepository.findAllByNameContainingAndIsDeletedFalse(productName, pageable);
            } else {
                data = productRepository.findAllByNameContainingAndCategoryAndIsDeletedFalse(productName, category, pageable);
            }
        } else if(category.equals("")) {
            data = productRepository.findAll(pageable);
        } else {
            data = productRepository.findAllByCategoryAndIsDeletedFalse(category, pageable);
        }

        return data.map(ProductDto::new);
    }

}
