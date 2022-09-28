package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.ProductDetail;
import com.feelmycode.parabole.domain.ProductDetailRepository;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
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
    private final SellerService sellerService;

    @Transactional
    public void createProductDetail(Long userId, ProductDetail productDetail) {
        sellerService.getSeller(userId);
        productDetailRepository.save(productDetail);
    }

    public List<ProductDetail> getProductDetailList(Long productId) {
        List<ProductDetail> productDetailList = productDetailRepository.findAllByProductId(productId);
        if(productDetailList == null || productDetailList.isEmpty())
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "상품 이미지를 찾을 수 없습니다.");
        return productDetailList;
    }

}
