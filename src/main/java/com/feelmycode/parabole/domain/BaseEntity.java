package com.feelmycode.parabole.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class BaseEntity {
    @Column(updatable = false)
    private String productCreaetedAt;

    private String productUpdateAt;
    @Column(updatable = false)
    private String productDeleteAt;
}
