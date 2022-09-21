package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_SIZE = 20;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public ResponseEntity<Page<Product>> getProductList(@RequestParam(required = false) Long sellerId,
                                            @RequestParam(required = false) String sellerName,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) String productName,
                                            @RequestParam(required = false) Pageable pageable) {

        Long getSellerId = 0L;
        String getSellerName = "";
        String getCategory = "";
        String getProductName = "";
        Pageable getPageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE);

        if(sellerId != null) {
            getSellerId = sellerId;
        }
        if(sellerName != null && !sellerName.equals("null") && !sellerName.equals("")) {
            getSellerName = sellerName;
        }
        if(category != null && !category.equals("null") && !category.equals("")) {
            getCategory = category;
        }
        if(productName != null && !productName.equals("null") && !productName.equals("")) {
            getProductName = productName;
        }
        if(pageable != null) {
            getPageable = pageable;
        }

        return ResponseEntity.ok(productService.getProductList(getSellerId, getSellerName, getCategory, getProductName, getPageable));
    }

}
