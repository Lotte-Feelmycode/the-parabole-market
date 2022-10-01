package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Product;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProductListGetResponseDto {
    private final Long productId;
    private final String productName;
    private final Long sellerId;
    private final Integer productStatus;
    private final Long productRemains;
    private final Long productPrice;
    private final String productCategory;
    private final String productThumbnailImg;
    private final LocalDateTime productCreatedAt;
    private final LocalDateTime productUpdatedAt;

    public ProductListGetResponseDto(Product product) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.sellerId = product.getSeller().getId();
        this.productStatus = product.getSalesStatus();
        this.productRemains = product.getRemains();
        this.productPrice = product.getPrice();
        this.productCategory = product.getCategory();
        this.productThumbnailImg = product.getThumbnailImg();
        this.productCreatedAt = product.getCreatedAt();
        this.productUpdatedAt = product.getUpdatedAt();
    }
}