package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.ProductDetail;
import lombok.Getter;

@Getter
public class ProductDetailDto {

    private final Long productDetailId;
    private final Long productId;
    private final String img;
    private final String imgCaption;

    public ProductDetailDto(ProductDetail productDetail) {
        this.productDetailId = productDetail.getId();
        this.productId = productDetail.getProduct().getId();
        this.img = productDetail.getImg();
        this.imgCaption = productDetail.getImgCaption();
    }

}
