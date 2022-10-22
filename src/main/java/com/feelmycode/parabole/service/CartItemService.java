package com.feelmycode.parabole.service;

import com.feelmycode.parabole.domain.Cart;
import com.feelmycode.parabole.domain.CartItem;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.dto.CartAddItemRequestDto;
import com.feelmycode.parabole.dto.CartItemDeleteRequestDto;
import com.feelmycode.parabole.dto.CartItemDto;
import com.feelmycode.parabole.dto.CartItemGetResponseDto;
import com.feelmycode.parabole.dto.CartItemListWithCouponDto;
import com.feelmycode.parabole.dto.CartItemUpdateRequestDto;
import com.feelmycode.parabole.dto.CouponInfoResponseDto;
import com.feelmycode.parabole.global.error.exception.ParaboleException;
import com.feelmycode.parabole.repository.CartItemRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final CouponService couponService;

    @Transactional
    public void addItem(CartAddItemRequestDto dto) {
        Cart cart = cartService.getCart(dto.getUserId());

        Product product = productService.getProduct(dto.getProductId());
        if(product.getRemains() < dto.getCnt()) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "재고보다 많습니다. 최대 "+product.getRemains()+"개 까지 구매 가능합니다.");
        }

        if(cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId()).isPresent()){
            CartItem cartItem = cartItemRepository.findByCartIdAndProductId(
                cart.getId(), product.getId()).get();
            updateItem(new CartItemUpdateRequestDto(
                cartItem.getId(), cartItem.getProduct().getId(), dto.getUserId(),
                dto.getCnt()));
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "이미 등록된 상품입니다.");
        }

        Long count = cartItemRepository.countByCartId(cart.getId());
        if(count >= 100) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "장바구니에 넣을 수 있는 물품이 최대 입니다.");
        }

        CartItem cartItem = new CartItem(cart, product, dto.getCnt());
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void updateItem(CartItemUpdateRequestDto cartItemDto) {
        CartItem currentCartItem = getCartItem(cartItemDto.getCartItemId());
        Product product = currentCartItem.getProduct();

        if(product.getRemains() < cartItemDto.getCnt()) {
            throw new ParaboleException(HttpStatus.BAD_REQUEST, "재고보다 많습니다. 최대 "+product.getRemains()+"개 까지 구매 가능합니다.");
        }

        currentCartItem.setCnt(cartItemDto.getCnt());
        cartItemRepository.save(currentCartItem);
    }

    public CartItemGetResponseDto getCartItemList(Long userId) {
        Cart cart = cartService.getCart(userId);
        List<CartItemDto> cartItemList = cartItemRepository.findAllByCartId(cart.getId())
            .stream().sorted(Comparator.comparing(CartItem::getId).reversed())
            .map(CartItemDto::new).toList();

        return new CartItemGetResponseDto(cart.getId(), cartItemList, cartItemList.size());
    }

    @Transactional
    public void cartListDelete(CartItemDeleteRequestDto cartItemRequestDto) {
        cartItemRepository.deleteById(cartItemRequestDto.getCartItemId());
    }

    private CartItem getCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new ParaboleException(HttpStatus.BAD_REQUEST, "장바구니에 상품이 존재하지 않습니다."));
    }


    // 셀러별로 항목 정렬
    public List<CartItemListWithCouponDto>[] getCartItemGroupBySellerIdOrderByIdDesc(Long userId) {
        Cart cart = cartService.getCart(userId);

        List<CartItemDto> getCartItems = cartItemRepository.findAllByCartId(cart.getId())
            .stream().sorted(Comparator.comparing(CartItem::getId).reversed())
            .map(CartItemDto::new).toList();

        HashMap<Long, Integer> sellerIdMap = new HashMap<>();
        int idx = 0;
        for(CartItemDto item : getCartItems) {
            Long sellerId = item.getProduct().getSellerId();
            if(!sellerIdMap.containsKey(sellerId)) {
                sellerIdMap.put(sellerId, idx++);
            }
        }

        List<CartItemDto>[] getItemList = new ArrayList[sellerIdMap.size()];
        for(int i = 0; i < sellerIdMap.size(); i++) {
            getItemList[i] = new ArrayList<>();
        }

        for (CartItemDto item : getCartItems) {
            Long sellerId = item.getProduct().getSellerId();
            getItemList[sellerIdMap.get(sellerId)].add(item);
        }

        HashMap<Long, List<CouponInfoResponseDto>> couponList = couponService.getCouponListByUserId(userId);

        List<CartItemListWithCouponDto>[] cartItemWithCoupon = new ArrayList[sellerIdMap.size()];

        for(int i = 0; i < sellerIdMap.size(); i++) {
            cartItemWithCoupon[i] = new ArrayList<>();
        }

        idx = 0;
        for(Long key : sellerIdMap.keySet()) {
            if(couponList.containsKey(key)) {
                cartItemWithCoupon[sellerIdMap.get(key)].add(new CartItemListWithCouponDto(key, getItemList[sellerIdMap.get(key)], couponList.get(key)));
            } else {
                cartItemWithCoupon[key.intValue()].add(new CartItemListWithCouponDto(key, getItemList[sellerIdMap.get(key)], new ArrayList<>()));
            }
        }

        return cartItemWithCoupon;
    }

}
