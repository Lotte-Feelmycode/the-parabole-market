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
@Table(name = "products")
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller sellers;

    @Column(name = "product_sales_status")
    private Integer productSalesStatus;

    @Column(name = "product_remains")
    private Long productRemains;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "product_thumbnail_img")
    private String productThumbnailImg;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private Long productPrice;

    public Product() {
    }

    public Product(Seller seller, Integer productSalesStatus, Long productRemains,
        String productCategory, String productThumbnailImg, String productName, Long productPrice) {
        this.sellers = seller;
        this.productSalesStatus = productSalesStatus;
        this.productRemains = productRemains;
        this.productCategory = productCategory;
        this.productThumbnailImg = productThumbnailImg;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public Product(Integer productSalesStatus, Long productRemains, String productCategory,
        String productThumbnailImg, String productName, Long productPrice) {
        this.productSalesStatus = productSalesStatus;
        this.productRemains = productRemains;
        this.productCategory = productCategory;
        this.productThumbnailImg = productThumbnailImg;
        this.productName = productName;
        this.productPrice = productPrice;
    }
}
