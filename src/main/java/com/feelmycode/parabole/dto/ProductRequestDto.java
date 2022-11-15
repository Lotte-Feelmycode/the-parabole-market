package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductRequestDto {

    private String productName;
    private Long productRemains;
    private Long productPrice;
    private String productCategory;
    private String productThumbnailImg;

    public ProductRequestDto(String productName, Long productRemains,
        Long productPrice,
        String productCategory, String productThumbnailImg) {
        this.productName = productName;
        this.productRemains = productRemains;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productThumbnailImg = productThumbnailImg;
    }

    // salesStatus
    public Product dtoToEntity() {
        return new Product(productName, productRemains, productPrice, productCategory, productThumbnailImg);
    }

}
