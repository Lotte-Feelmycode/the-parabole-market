package com.feelmycode.parabole.service;


import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
class ProductServiceTest {
/*
    @Autowired ProductService productService;
    @Autowired
    ProductRepository productRepository;

    private static Seller seller = new Seller("테스트", "1");
    @Test
    void saveProduct() {
        // given
        Product product = new Product(seller, 1, 50L, "국밥", "https://img.url", "순대국밥", 15000L);

        // when
        productService.saveProduct(1L, product);

        // then
        Product getProduct = productService.getProduct(product.getId());
        assertEquals("상품 상태", product.getSalesStatus(), getProduct.getSalesStatus());
        assertEquals("상품 수량", product.getRemains(), getProduct.getRemains());
        assertEquals("상품 카테고리", product.getCategory(), getProduct.getCategory());
        assertEquals("상품 썸네일", product.getThumbnailImg(), getProduct.getThumbnailImg());
        assertEquals("상품 이름", "순대국밥", getProduct.getName());
        assertEquals("상품 가격", product.getPrice(), getProduct.getPrice());
    }

    @Test
    public void updateTest() throws Exception {
        // given
        Product product = new Product(1L, seller, 1, 50L, "국밥", "https://img.url", "순대국밥", 2000L);

        // when
        Long getId = productService.updateProduct(1L, product);

        //then
        Product getProduct = productService.getProduct(getId);

        assertEquals("상품 상태", product.getSalesStatus(), getProduct.getSalesStatus());
        assertEquals("상품 수량", product.getRemains(), getProduct.getRemains());
        assertEquals("상품 카테고리", product.getCategory(), getProduct.getCategory());
        assertEquals("상품 썸네일", product.getThumbnailImg(), getProduct.getThumbnailImg());
        assertEquals("상품 이름", "순대국밥", getProduct.getName());
        assertEquals("상품 가격", product.getPrice(), getProduct.getPrice());
    }

    @Test
    @DisplayName("상품 생성")
    public void createProduct() {

        // given
        Long userId = 1L;
        Product product = new Product(new Seller(1L, "store"), 1, 50L,
            "국밥", "img.jpg", "순대국밥", 10000L);

        //when
        Long productId = productService.saveProduct(1L, new ProductDto(product));

        //then
        Product getProduct = productRepository.findById(productId).orElseThrow();
        assertEquals(productId, getProduct.getId());

        product

    }
*/
}
