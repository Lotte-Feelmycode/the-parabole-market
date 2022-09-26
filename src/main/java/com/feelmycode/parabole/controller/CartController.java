package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.CartItem;
import com.feelmycode.parabole.dto.CartItemDto;
import com.feelmycode.parabole.service.CartItemService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartItemService cartItemService;

    @ExceptionHandler(value = Exception.class)
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<List<CartItem>> cartList(@Valid Long userId){

        return ResponseEntity.ok(cartItemService.cartItemList(userId));
    }

    @ExceptionHandler(value = Exception.class)
    @RequestMapping(value = "/addproduct", method = RequestMethod.POST)
    public String addProductInCart(CartItemDto cartItemDto){
        if(cartItemService.addItem(cartItemDto).equals("장바구니 성공")){
            return "성공";
        }
        return "실패";
    }
}
