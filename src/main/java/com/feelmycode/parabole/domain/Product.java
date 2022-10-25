package com.feelmycode.parabole.domain;

import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductDetail> productDetailList = new ArrayList<>();

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

    private void setProductDetailList(List<ProductDetail> detailList) {
        this.productDetailList = detailList;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Product setProduct(Product getProduct) {
        this.setName(getProduct.getName());
        this.setPrice(getProduct.getPrice());
        this.setCategory(getProduct.getCategory());
        this.setName(getProduct.getName());
        this.setRemains(getProduct.getRemains());
        this.setSalesStatus(getProduct.getSalesStatus());
        this.setThumbnailImg(getProduct.getThumbnailImg());
        this.setProductDetailList(getProduct.getProductDetailList());
        return this;
    }

    public Product(Seller seller, Integer salesStatus, Long remains,
        String category, String thumbnailImg, String name, Long price) {
        this.seller = seller;
        this.salesStatus = salesStatus;
        this.remains = remains;
        this.category = category;
        this.thumbnailImg = thumbnailImg;
        this.name = name;
        this.price = price;
    }

    public Product(Long id, Seller seller, Integer salesStatus, Long remains,
        String category, String thumbnailImg, String name, Long price) {
        this.id = id;
        this.seller = seller;
        this.salesStatus = salesStatus;
        this.remains = remains;
        this.category = category;
        this.thumbnailImg = thumbnailImg;
        this.name = name;
        this.price = price;
    }

    public Product(Seller seller, String name, Integer salesStatus, Long remains, Long price,
        String category, String thumbnailImg, List<ProductDetail> productDetailList) {
        this.seller = seller;
        this.name = name;
        this.salesStatus = salesStatus;
        this.remains = remains;
        this.price = price;
        this.category = category;
        this.thumbnailImg = thumbnailImg;
        this.productDetailList = productDetailList;
    }

}
