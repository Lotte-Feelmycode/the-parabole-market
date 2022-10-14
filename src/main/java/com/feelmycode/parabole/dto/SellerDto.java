package com.feelmycode.parabole.dto;

import com.feelmycode.parabole.domain.Seller;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SellerDto {
    private Long sellerId;
    private Long userId;
    private String storeName;
    private String registrationNo;

    public SellerDto(Seller seller) {
        this.sellerId = seller.getId();
        this.userId = seller.getUser().getId();
        this.storeName = seller.getStoreName();
        this.registrationNo = seller.getRegistrationNo();
    }
}
