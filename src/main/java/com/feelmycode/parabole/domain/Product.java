package com.feelmycode.parabole.domain;

import com.sun.istack.NotNull;
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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "seller_id")
//    private Seller sellerId;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "product_sales_status")
    @NotNull
    private Integer productSalesStatus;

    @Column(name = "product_remains")
    @NotNull
    private Long productRemains;

    @Column(name = "product_category")
    @NotNull
    private String productCategory;

    @Column(name = "product_thumbnail_img")
    @NotNull
    private String productThumbnailImg;

    @Column(name = "product_name")
    @NotNull
    private String productName;

    @Column(name = "product_price")
    @NotNull
    private Long productPrice;

    public Product() {
    }

    private void setProductSalesStatus(Integer productSalesStatus) {
        this.productSalesStatus = productSalesStatus;
    }

    private void setProductRemains(Long productRemains) {
        this.productRemains = productRemains;
    }

    private void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    private void setProductThumbnailImg(String productThumbnailImg) {
        this.productThumbnailImg = productThumbnailImg;
    }

    private void setProductName(String productName) {
        this.productName = productName;
    }

    private void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }

    public Product setProduct(Product getProduct) {
        this.setProductName(getProduct.getProductName());
        this.setProductPrice(getProduct.getProductPrice());
        this.setProductCategory(getProduct.getProductCategory());
        this.setProductName(getProduct.getProductName());
        this.setProductRemains(getProduct.getProductRemains());
        this.setProductSalesStatus(getProduct.getProductSalesStatus());
        this.setProductThumbnailImg(getProduct.getProductThumbnailImg());
        return this;
    }

    public Product(Long sellerId, Integer productSalesStatus, Long productRemains,
        String productCategory, String productThumbnailImg, String productName, Long productPrice) {
        this.sellerId = sellerId;
        this.productSalesStatus = productSalesStatus;
        this.productRemains = productRemains;
        this.productCategory = productCategory;
        this.productThumbnailImg = productThumbnailImg;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public Product(Long id, Long sellerId, Integer productSalesStatus, Long productRemains,
        String productCategory, String productThumbnailImg, String productName, Long productPrice) {
        this.id = id;
        this.sellerId = sellerId;
        this.productSalesStatus = productSalesStatus;
        this.productRemains = productRemains;
        this.productCategory = productCategory;
        this.productThumbnailImg = productThumbnailImg;
        this.productName = productName;
        this.productPrice = productPrice;
    }
}
