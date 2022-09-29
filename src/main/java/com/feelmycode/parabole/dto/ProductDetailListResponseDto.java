package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.ProductDetail;
import com.feelmycode.parabole.domain.Seller;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailListResponseDto {

    private Product product;
    private List<ProductDetail> productDetail = new ArrayList<>();
    private Seller seller;

    public ProductDetailListResponseDto(Product product, List<ProductDetail> productDetail, Seller seller) {
        this.product = product;
        this.productDetail = productDetail;
        this.seller = seller;
    }
}
