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

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller sellerId;

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


}
