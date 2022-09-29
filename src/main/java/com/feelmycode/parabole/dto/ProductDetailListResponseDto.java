package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.ProductDetail;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailListResponseDto {

    private Product product;
    private List<ProductDetail> productDetail = new ArrayList<>();

    public ProductDetailListResponseDto(Product product, List<ProductDetail> productDetail) {
        this.product = product;
        this.productDetail = productDetail;
    }
}
