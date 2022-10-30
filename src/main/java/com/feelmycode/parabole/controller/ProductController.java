package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.ProductDetail;
import com.feelmycode.parabole.dto.ProductDetailListResponseDto;
import com.feelmycode.parabole.dto.ProductDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.global.util.StringUtil;
import com.feelmycode.parabole.service.ProductDetailService;
import com.feelmycode.parabole.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product")
@Slf4j
public class ProductController {

    private final static int DEFAULT_SIZE = 20;
    private final ProductService productService;
    private final ProductDetailService productDetailService;

    // TODO: DTO를 사용해서 parameter를 깔끔하게 받고 한번에 NULL처리를 해서 초기화하기
    // +@ Valid를 custom해서 validation할 때 인터페이스 받아서 커스텀으로 초기화할 수도 있음
    @GetMapping("/list")
    public ResponseEntity<ParaboleResponse> getProductList(@RequestParam(required = false) String sellerId,
                                            @RequestParam(required = false) String storeName,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) String productName,
                                            @PageableDefault(size = DEFAULT_SIZE) Pageable pageable) {

        Long getSellerId = 0L;
        String getStoreName = StringUtil.controllerParamIsBlank(storeName) ? "" : storeName;
        String getCategory = StringUtil.controllerParamIsBlank(category) ? "" : category;
        String getProductName = StringUtil.controllerParamIsBlank(productName) ? "" : productName;

        if(!StringUtil.controllerParamIsBlank(sellerId)) {
            try {
                getSellerId = Long.parseLong(sellerId);
            } catch (NumberFormatException e) {
                throw new ParaboleException(HttpStatus.BAD_REQUEST, "잘못된 판매자 id 입니다. 상품목록 조회에 실패했습니다.");
            }
        }

        log.debug("getSellerId : {} / getStoreName : {} / getProductName : {} / getCategory : {} / getPageable : {}", getSellerId, getStoreName, getProductName, getCategory, pageable);
        Page<ProductDto> response = productService.getProductList(getSellerId, getStoreName,
            getProductName, getCategory, pageable);

        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "상품 전시", response);
    }

    @PostMapping
    public ResponseEntity<ParaboleResponse> createProduct(@RequestParam Long userId, @RequestBody ProductDto product) {
        productService.saveProduct(userId, product);
        
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "상품 생성");
    }

//    @PatchMapping
//    public ResponseEntity<ParaboleResponse> updateProduct(@RequestParam Long userId, @RequestBody Product product) {
//        productService.updateProduct(userId, product);
//
//        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "상품정보 수정");
//    }

    @GetMapping
    public ResponseEntity<ParaboleResponse> getProduct(@RequestParam Long productId) {
        ProductDetailListResponseDto response = productService.getProductDetail(productId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "상품 상세 정보", response);
    }

    @GetMapping("/seller/list")
    public ResponseEntity<ParaboleResponse> getProductBySellerId(@RequestParam Long userId, @PageableDefault(size = DEFAULT_SIZE) Pageable pageable) {
        log.info("Get Product By Seller Id : {} ", userId);
        Page<ProductDto> response = productService.getProductList(userId, "", "", "", pageable);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "판매자가 등록한 상품 목록", response);
    }

}
