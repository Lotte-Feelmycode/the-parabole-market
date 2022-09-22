package com.feelmycode.parabole.domain;

import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    // TODO: sellerId가 아닌 Seller 자체를 받을 수 있도록 수정할 예정
    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "product_name")
    @NotNull
    private String name;

    @Column(name = "product_sales_status")
    @NotNull
    private Integer salesStatus;

    @Column(name = "product_remains")
    @NotNull
    private Long remains;

    @Column(name = "product_price")
    @NotNull
    private Long price;

    @Column(name = "product_category")
    @NotNull
    private String category;

    @Column(name = "product_thumbnail_img")
    @NotNull
    private String thumbnailImg;

    @OneToMany(mappedBy = "product")
    private List<ProductDetail> productDetailList = new ArrayList<>();


    public Product() {
    }

    private void setSalesStatus(Integer salesStatus) {
        this.salesStatus = salesStatus;
    }

    private void setRemains(Long remains) {
        this.remains = remains;
    }

    private void setCategory(String category) {
        this.category = category;
    }

    private void setThumbnailImg(String thumbnailImg) {
        this.thumbnailImg = thumbnailImg;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setPrice(Long price) {
        this.price = price;
    }

    public Product setProduct(Product getProduct) {
        this.setName(getProduct.getName());
        this.setPrice(getProduct.getPrice());
        this.setCategory(getProduct.getCategory());
        this.setName(getProduct.getName());
        this.setRemains(getProduct.getRemains());
        this.setSalesStatus(getProduct.getSalesStatus());
        this.setThumbnailImg(getProduct.getThumbnailImg());
        return this;
    }

    public Product(Long sellerId, Integer salesStatus, Long remains,
        String category, String thumbnailImg, String name, Long price) {
        this.sellerId = sellerId;
        this.salesStatus = salesStatus;
        this.remains = remains;
        this.category = category;
        this.thumbnailImg = thumbnailImg;
        this.name = name;
        this.price = price;
    }

    public Product(Long id, Long sellerId, Integer salesStatus, Long remains,
        String category, String thumbnailImg, String name, Long price) {
        this.id = id;
        this.sellerId = sellerId;
        this.salesStatus = salesStatus;
        this.remains = remains;
        this.category = category;
        this.thumbnailImg = thumbnailImg;
        this.name = name;
        this.price = price;
    }

}
