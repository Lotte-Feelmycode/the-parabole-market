package com.feelmycode.parabole.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.dto.ProductDto;
import com.feelmycode.parabole.repository.ProductRepository;
import com.feelmycode.parabole.service.ProductService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductControllerTest {

    @LocalServerPort
    int port;
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private RequestSpecification spec;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setUp() {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
            .build();
    }

    @Test
    @DisplayName("상품 목록 테스트")
    public void productList() throws Exception {
        given(this.spec)
            .param("sellerId", "")
            .param("storeName", "")
            .param("category", "")
            .param("productName", "")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("product-list",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("sellerId").description("셀러 이름"),
                    parameterWithName("storeName").description("셀러 스토어 이름"),
                    parameterWithName("category").description("카테고리"),
                    parameterWithName("productName").description("상품명")
                    )
                )/*,*/
//                responseFields(
//                    fieldWithPath()
//                )
            )
            .when()
            .port(port)
            .get("/api/v1/product/list");
    }

    @Test
    @DisplayName("상품 목록 생성")
    public void createProduct() {
        // given
        Long userId = 1L;
        Product product = new Product(new Seller(1L, "store"), 1, 50L,
            "국밥", "img.jpg", "순대국밥", 10000L);

        //when
        Long productId = productService.saveProduct(1L, new ProductDto(product));

        //then
        Product getProduct = productRepository.findById(productId).orElseThrow();
        assertThat(productId).isEqualTo(getProduct.getId());

    }
}