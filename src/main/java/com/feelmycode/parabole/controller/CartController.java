package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.dto.CartItemDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartItemService cartItemService;

    @PostMapping(value = "/list")
    public ResponseEntity<ParaboleResponse> cartList(@RequestParam Long userId) {
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "장바구니 리스트",
            cartItemService.cartItemList(userId));
    }

    @PostMapping(value = "/product/add")
    public ResponseEntity<ParaboleResponse> addProductInCart(@RequestBody CartItemDto cartItemDto) {
        cartItemService.addItem(cartItemDto);
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "장바구니 상품 추가");
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ParaboleResponse> deleteProductInCart(
        @RequestBody CartItemDto cartItemDto) {
        System.out.println(cartItemDto.toString());
        cartItemService.cartListDelete(cartItemDto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "장바구니 상품 삭제");
    }

    @PatchMapping(value = "/update/cnt")
    public ResponseEntity<ParaboleResponse> updateProductCnt(@RequestBody CartItemDto cartItemDto) {
        cartItemService.updateItem(cartItemDto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "상품수량 수정");
    }
}
