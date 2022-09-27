package com.feelmycode.parabole.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    SELLER("ROLE_SELLER", "판매자"),
    USER("ROLE_USER", "사용자"),
    ADMIN("ROLE_ADMIN","관리자");

    private final String key;
    private final String title;

}