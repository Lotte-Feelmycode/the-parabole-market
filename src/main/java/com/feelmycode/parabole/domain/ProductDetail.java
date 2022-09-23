package com.feelmycode.parabole.domain;

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
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_detail_img")
    private String img;

    @Column(name = "product_detail_img_caption")
    private String imgCaption;

    private void setProduct(Product product) {
        this.product = product;
    }

    private void setImg(String img) {
        this.img = img;
    }

    private void setImgCaption(String imgCaption) {
        this.imgCaption = imgCaption;
    }

    public ProductDetail() {
    }

    public ProductDetail(Product product, String img, String imgCaption) {
        this.product = product;
        this.img = img;
        this.imgCaption = imgCaption;
    }

    public ProductDetail setProductDetail(Product product, String img, String imgCaption) {
        this.setProduct(product);
        this.setImg(img);
        this.setImgCaption(imgCaption);
        return this;
    }

}
