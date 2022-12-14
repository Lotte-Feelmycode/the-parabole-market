package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProductDto {
    private final Long productId;
    private final String productName;
    private final Long sellerId;
    private final String storeName;
    private final Integer productStatus;
    private final Long productRemains;
    private final Long productPrice;
    private final String productCategory;
    private final String productThumbnailImg;
    private final LocalDateTime productCreatedAt;
    private final LocalDateTime productUpdatedAt;
    private final LocalDateTime productDeletedAt;
    private final boolean productIsDeleted;

    public ProductDto(Product product) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.storeName = product.getSeller().getStoreName();
        this.sellerId = product.getSeller().getId();
        this.productStatus = product.getSalesStatus();
        this.productRemains = product.getRemains();
        this.productPrice = product.getPrice();
        this.productCategory = product.getCategory();
        this.productThumbnailImg = product.getThumbnailImg();
        this.productCreatedAt = product.getCreatedAt();
        this.productUpdatedAt = product.getUpdatedAt();
        this.productDeletedAt = product.getDeletedAt();
        this.productIsDeleted = product.isDeleted();
    }

    public Product dtoToEntity() {
        return new Product(productId, new Seller(sellerId, storeName), productStatus, productRemains, productCategory,
            productThumbnailImg, productName, productPrice);
    }
}
