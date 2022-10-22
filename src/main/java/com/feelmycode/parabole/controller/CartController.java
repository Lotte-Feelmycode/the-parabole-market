package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.dto.CartAddItemRequestDto;
import com.feelmycode.parabole.dto.CartItemDeleteRequestDto;
import com.feelmycode.parabole.dto.CartItemGetResponseDto;
import com.feelmycode.parabole.dto.CartItemListWithCouponDto;
import com.feelmycode.parabole.dto.CartItemUpdateRequestDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.CartItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "/list")
    public ResponseEntity<ParaboleResponse> cartList(@RequestParam Long userId) {
        CartItemGetResponseDto response = cartItemService.getCartItemList(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "장바구니 리스트", response);
    }

    @PostMapping(value = "/product/add")
    public ResponseEntity<ParaboleResponse> addProductInCart(@RequestBody CartAddItemRequestDto cartItemDto) {
        cartItemService.addItem(cartItemDto);
        return ParaboleResponse.CommonResponse(HttpStatus.CREATED, true, "장바구니 상품 추가");
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<ParaboleResponse> deleteProductInCart(
        @RequestParam Long userId, @RequestParam Long cartItemId) {
        cartItemService.cartListDelete(new CartItemDeleteRequestDto(userId, cartItemId));
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "장바구니 상품 삭제");

    }

    @PatchMapping(value = "/update/cnt")
    public ResponseEntity<ParaboleResponse> updateProductCnt(@RequestBody CartItemUpdateRequestDto dto) {
        cartItemService.updateItem(dto);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "상품수량 수정");
    }

    // 상품정보와 쿠폰을 셀러기준으로 grouping하여 가져옴
    @GetMapping(value="/items")
    public ResponseEntity<ParaboleResponse> getCartItems(@RequestParam Long userId) {
        List<CartItemListWithCouponDto>[] cartItems = cartItemService.getCartItemGroupBySellerIdOrderByIdDesc(userId);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "seller로 grouping한 장바구니 상품", cartItems);
    }

}
