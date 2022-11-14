package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.ProductDetail;

public class ProductDetailRequestDto {

    private Long productId;
    private String imgUrl;
    private String imgCaption;

    public ProductDetailRequestDto(Long productId, String imgUrl, String imgCaption) {
        this.productId = productId;
        this.imgUrl = imgUrl;
        this.imgCaption = imgCaption;
    }

    public ProductDetail toDto() {
        return new ProductDetail(new Product(productId), imgUrl, imgCaption);
    }
}
