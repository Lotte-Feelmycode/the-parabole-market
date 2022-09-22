package com.feelmycode.parabole.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "product_details")
@Getter
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetailId;

    @Column(name = "product_detail_img")
    private String Img;

    @Column(name = "product_detail_img_caption")
    private String imgCaption;

    public ProductDetail() {
    }

    public ProductDetail(ProductDetail productDetailId, String img, String imgCaption) {
        this.productDetailId = productDetailId;
        Img = img;
        this.imgCaption = imgCaption;
    }

}
