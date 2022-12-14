package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.ProductDetail;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.ProductDetailRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;

    @Transactional
    public void createProductDetail(ProductDetail productDetail) {
        productDetailRepository.save(productDetail);
    }

    public List<ProductDetail> getProductDetailList(Long productId) {
        List<ProductDetail> productDetailList = productDetailRepository.findAllByProductId(productId);
        return productDetailList;
    }

}
