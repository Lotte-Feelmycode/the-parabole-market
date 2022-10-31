package com.feelmycode.parabole.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
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
    @DisplayName("판매자의 상품 리스트 가져오기")
    public void getProductBySellerId() {
        given(this.spec)
            .param("productId", "1")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("get-product-by-seller",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자(판매자) ID")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환하는 데이터 정보"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("상품 정보"),
                    fieldWithPath("data.content.productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                    fieldWithPath("data.content.productName").type(JsonFieldType.STRING).description("상품 명"),
                    fieldWithPath("data.content.sellerId").type(JsonFieldType.NUMBER).description("판매자 ID"),
                    fieldWithPath("data.content.storeName").type(JsonFieldType.STRING).description("판매자의 상호 이름"),
                    fieldWithPath("data.content.productStatus").type(JsonFieldType.NUMBER).description("상품의 상태"),
                    fieldWithPath("data.content.productRemains").type(JsonFieldType.NUMBER).description("상품 재고"),
                    fieldWithPath("data.content.productPrice").type(JsonFieldType.NUMBER).description("상품 가격"),
                    fieldWithPath("data.content.productCategory").type(JsonFieldType.STRING).description("상품의 카테고리"),
                    fieldWithPath("data.content.productThumbnailImg").type(JsonFieldType.STRING).description("상품의 썸네일 이미지"),
                    fieldWithPath("data.content.productCreatedAt").type(JsonFieldType.STRING).description("상품의 생성 일자"),
                    fieldWithPath("data.content.productUpdatedAt").type(JsonFieldType.STRING).description("상품의 수정 일자"),
                    fieldWithPath("data.content.productDeletedAt").type(JsonFieldType.STRING).description("상품의 삭제 일자"),
                    fieldWithPath("data.content.productIsDeleted").type(JsonFieldType.STRING).description("상품의 삭제 여부")
                )
            ))
            .when()
            .port(port)
            .get("/api/v1/product/seller/list");
    }

    @Test
    @DisplayName("상품 정보 가져오기")
    public void getProduct() {
        given(this.spec)
            .param("productId", "1")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("get-product",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 ID")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환하는 데이터 정보"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("상품 정보"),
                    fieldWithPath("data.content.productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                    fieldWithPath("data.content.productName").type(JsonFieldType.STRING).description("상품 명"),
                    fieldWithPath("data.content.storeName").type(JsonFieldType.STRING).description("판매자 상호 이름"),
                    fieldWithPath("data.content.sellerId").type(JsonFieldType.NUMBER).description("판매자 ID"),
                    fieldWithPath("data.content.storeName").type(JsonFieldType.STRING).description("판매자의 상호 이름"),
                    fieldWithPath("data.content.productStatus").type(JsonFieldType.NUMBER).description("상품의 상태"),
                    fieldWithPath("data.content.productRemains").type(JsonFieldType.NUMBER).description("상품 재고"),
                    fieldWithPath("data.content.productPrice").type(JsonFieldType.NUMBER).description("상품 가격"),
                    fieldWithPath("data.content.productCategory").type(JsonFieldType.STRING).description("상품의 카테고리"),
                    fieldWithPath("data.content.productThumbnailImg").type(JsonFieldType.STRING).description("상품의 썸네일 이미지"),
                    fieldWithPath("data.content.productCreatedAt").type(JsonFieldType.STRING).description("상품의 생성 일자"),
                    fieldWithPath("data.content.productUpdatedAt").type(JsonFieldType.STRING).description("상품의 수정 일자"),
                    fieldWithPath("data.content.productDeletedAt").type(JsonFieldType.STRING).description("상품의 삭제 일자"),
                    fieldWithPath("data.content.productIsDeleted").type(JsonFieldType.STRING).description("상품의 삭제 여부"),
                    fieldWithPath("data.content.productDetail").type(JsonFieldType.STRING).description("상품 이미지 정보"),
                    fieldWithPath("data.content.productDetail.[].productDetailId").type(JsonFieldType.NUMBER).description("상품 상세 ID"),
                    fieldWithPath("data.content.productDetail.[].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                    fieldWithPath("data.content.productDetail.[].img").type(JsonFieldType.STRING).description("상품 이미지"),
                    fieldWithPath("data.content.productDetail.[].imgCaption").type(JsonFieldType.STRING).description("상품 이미지 상세 설명")
                )
            ))
            .when()
            .port(port)
            .get("/api/v1/product");
        ;

    }

    @Test
    @DisplayName("상품 생성")
    public void createProduct() {
        given(this.spec)
            .param("userId", "1")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("create-product",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("ProductDto").type(JsonFieldType.OBJECT).description("상품정보"),
                    fieldWithPath("ProductDto.productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                    fieldWithPath("ProductDto.productName").type(JsonFieldType.STRING).description("상품 명"),
                    fieldWithPath("ProductDto.sellerId").type(JsonFieldType.NUMBER).description("판매자 ID"),
                    fieldWithPath("ProductDto.storeName").type(JsonFieldType.STRING).description("판매자의 상호 이름"),
                    fieldWithPath("ProductDto.productStatus").type(JsonFieldType.NUMBER).description("상품의 상태"),
                    fieldWithPath("ProductDto.productRemains").type(JsonFieldType.NUMBER).description("상품 재고"),
                    fieldWithPath("ProductDto.productPrice").type(JsonFieldType.NUMBER).description("상품 가격"),
                    fieldWithPath("ProductDto.productCategory").type(JsonFieldType.STRING).description("상품의 카테고리"),
                    fieldWithPath("ProductDto.productThumbnailImg").type(JsonFieldType.STRING).description("상품의 썸네일 이미지"),
                    fieldWithPath("ProductDto.productCreatedAt").type(JsonFieldType.STRING).description("상품의 생성 일자"),
                    fieldWithPath("ProductDto.productUpdatedAt").type(JsonFieldType.STRING).description("상품의 수정 일자"),
                    fieldWithPath("ProductDto.productDeletedAt").type(JsonFieldType.STRING).description("상품의 삭제 일자"),
                    fieldWithPath("ProductDto.productIsDeleted").type(JsonFieldType.BOOLEAN).description("상품의 삭제 여부")
                )
            ))
            .when()
            .port(port)
            .post("/api/v1/product");
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
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공여부"),
                        fieldWithPath("message").description("메세지"),
                        fieldWithPath("data").description("상품 목록"),
                        fieldWithPath("data.content").description("상품"),
                        fieldWithPath("data.content.[].productId").description("상품 아이디"),
                        fieldWithPath("data.content.[].productName").description("상품 명"),
                        fieldWithPath("data.content.[].sellerId").description("셀러 아이디"),
                        fieldWithPath("data.content.[].storeName").description("셀러 스토어 이름"),
                        fieldWithPath("data.content.[].productStatus").description("상품 상태"),
                        fieldWithPath("data.content.[].productRemains").description("상품 재고"),
                        fieldWithPath("data.content.[].productPrice").description("상품 가격"),
                        fieldWithPath("data.content.[].productCategory").description("상품 카테고리"),
                        fieldWithPath("data.content.[].productThumbnailImg").description("상품 썸네일"),
                        fieldWithPath("data.content.[].productCreatedAt").description("생성일자"),
                        fieldWithPath("data.content.[].productUpdatedAt").description("수정일자"),
                        fieldWithPath("data.content.[].productDeletedAt").description("삭제일자"),
                        fieldWithPath("data.content.[].productIsDeleted").description("삭제여부"),
                        fieldWithPath("data.pageable").description("페이징 처리"),
                        fieldWithPath("data.pageable.sort").description("페이징 처리"),
                        fieldWithPath("data.pageable.sort.empty").description("페이징 처리"),
                        fieldWithPath("data.pageable.sort.unsorted").description("페이징 처리"),
                        fieldWithPath("data.pageable.sort.sorted").description("페이징 처리"),
                        fieldWithPath("data.pageable.offset").description("페이징 처리"),
                        fieldWithPath("data.pageable.pageNumber").description("페이징 처리"),
                        fieldWithPath("data.pageable.pageSize").description("페이징 처리"),
                        fieldWithPath("data.pageable.unpaged").description("페이징 처리"),
                        fieldWithPath("data.pageable.paged").description("페이징 처리"),
                        fieldWithPath("data.totalElements").description("총 항목 수"),
                        fieldWithPath("data.totalPages").description("총 페이지 수"),
                        fieldWithPath("data.last").description(""),
                        fieldWithPath("data.size").description(""),
                        fieldWithPath("data.number").description(""),
                        fieldWithPath("data.sort").description(""),
                        fieldWithPath("data.sort.empty").description(""),
                        fieldWithPath("data.sort.unsorted").description(""),
                        fieldWithPath("data.sort.sorted").description(""),
                        fieldWithPath("data.numberOfElements").description("총 개수"),
                        fieldWithPath("data.first").description(""),
                        fieldWithPath("data.empty").description("")
                    )
                )
            )
            .when()
            .port(port)
            .get("/api/v1/product/list");
    }

    @Test
    @DisplayName("상품 목록 생성")
    public void serviceCreateProduct() {
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
