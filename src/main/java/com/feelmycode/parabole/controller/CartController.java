package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.dto.CartAddItemRequestDto;
import com.feelmycode.parabole.dto.CartResponseDto;
import com.feelmycode.parabole.dto.CartItemDeleteRequestDto;
import com.feelmycode.parabole.dto.CartItemUpdateRequestDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartItemService cartItemService;

    @PostMapping(value = "/product/add")
    public ResponseEntity<ParaboleResponse> addProductInCart(@RequestAttribute("userId") Long userId, @RequestBody CartAddItemRequestDto cartItemDto) {
        log.info("addProductInCart : {}", cartItemDto.toString());
        cartItemService.addItem(userId, cartItemDto);
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "장바구니 상품 추가");
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ParaboleResponse> deleteProductInCart(
        @RequestAttribute("userId") Long userId, @RequestParam Long cartItemId) {
        cartItemService.cartListDelete(new CartItemDeleteRequestDto(userId, cartItemId));
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "장바구니 상품 삭제");

    }

    @PatchMapping(value = "/update/cnt")
    public ResponseEntity<ParaboleResponse> updateProductCnt(@RequestAttribute("userId") Long userId, @RequestBody CartItemUpdateRequestDto dto) {
        cartItemService.updateItem(userId, dto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "상품수량 수정");
    }

    // 상품정보와 쿠폰을 셀러기준으로 grouping하여 가져옴
    @GetMapping(value="/list")
    public ResponseEntity<ParaboleResponse> getCartItems(@RequestAttribute("userId") Long userId) {
        CartResponseDto cartResponseDto = cartItemService.getCartItemGroupBySellerIdOrderByIdDesc(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "seller로 grouping한 장바구니 상품", cartResponseDto);
    }

}
