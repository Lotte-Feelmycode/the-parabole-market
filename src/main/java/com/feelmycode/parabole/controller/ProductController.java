package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Message;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.service.ProductService;
import java.nio.charset.Charset;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_SIZE = 20;

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

    // TODO: 셀러정보를 받아서 product 추가하기
    @PostMapping
    public ResponseEntity createProduct(@RequestBody Product product) {
        productService.saveProduct(product);
        Message message = new Message();
        message.setMessage("테스트 메세지 전송");
        message.setSuccess(true);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, header, HttpStatus.OK);
    }

    // TODO: 셀러정보를 받아서 product 수정하기
    @PatchMapping
    public ResponseEntity updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);

        Message message = new Message();
        message.setMessage("테스트 메세지 전송");
        message.setSuccess(true);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, header, HttpStatus.OK);
    }

}
