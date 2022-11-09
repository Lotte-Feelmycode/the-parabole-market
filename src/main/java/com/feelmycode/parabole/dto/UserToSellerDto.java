package com.feelmycode.parabole.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserToSellerDto {

    private Long userId;
    private String storeName;
    private String registrationNo;

    public UserToSellerDto(Long userId, String storeName, String registrationNo) {
        this.userId = userId;
        this.storeName = storeName;
        this.registrationNo = registrationNo;
    }
}
