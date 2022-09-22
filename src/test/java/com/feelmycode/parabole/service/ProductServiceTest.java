package com.feelmycode.parabole.service;


import static org.junit.Assert.assertEquals;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
class ProductServiceTest {

    @Autowired ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @Test
    void saveProduct() {
        // given
        Product product = new Product(1L, 1, 50L, "국밥", "https://img.url", "순대국밥", 15000L);

        // when
        productService.saveProduct(product);

        // then
        Product getProduct = productService.getProduct(product.getId());
        assertEquals("상품 상태", product.getProductSalesStatus(), getProduct.getProductSalesStatus());
        assertEquals("상품 수량", product.getProductRemains(), getProduct.getProductRemains());
        assertEquals("상품 카테고리", product.getProductCategory(), getProduct.getProductCategory());
        assertEquals("상품 썸네일", product.getProductThumbnailImg(), getProduct.getProductThumbnailImg());
        assertEquals("상품 이름", "순대국밥", getProduct.getProductName());
        assertEquals("상품 가격", product.getProductPrice(), getProduct.getProductPrice());
    }

    @Test
    public void updateTest() throws Exception {
        // given
        Product product = new Product(1L, 1L, 1, 50L, "국밥", "https://img.url", "순대국밥", 2000L);

        // when
        Long getId = productService.updateProduct(product);

        //then
        Product getProduct = productService.getProduct(getId);

        assertEquals("상품 상태", product.getProductSalesStatus(), getProduct.getProductSalesStatus());
        assertEquals("상품 수량", product.getProductRemains(), getProduct.getProductRemains());
        assertEquals("상품 카테고리", product.getProductCategory(), getProduct.getProductCategory());
        assertEquals("상품 썸네일", product.getProductThumbnailImg(), getProduct.getProductThumbnailImg());
        assertEquals("상품 이름", "순대국밥", getProduct.getProductName());
        assertEquals("상품 가격", product.getProductPrice(), getProduct.getProductPrice());
    }

}