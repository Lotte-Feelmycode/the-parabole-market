package com.feelmycode.parabole.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ProductDetailListResponseDto {

    private ProductDto product;
    private List<ProductDetailDto> productDetail;
    private String storeName;

    public ProductDetailListResponseDto(ProductDto product, List<ProductDetailDto> productDetail, String storeName) {
        this.product = product;
        this.productDetail = productDetail;
        this.storeName = storeName;
    }

}
