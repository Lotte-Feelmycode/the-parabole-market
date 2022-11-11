package com.feelmycode.parabole.controller;

import static io.restassured.RestAssured.given;
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

import com.feelmycode.parabole.dto.ProductRequestDto;
import com.feelmycode.parabole.global.util.StringUtil;
import com.feelmycode.parabole.repository.ProductRepository;
import com.feelmycode.parabole.service.ProductService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class ProductControllerTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @LocalServerPort
    int port;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(StringUtil.ASCII_DOC_OUTPUT_DIR);

    private RequestSpecification spec;

    @Before
    public void setUp() {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
            .build();
    }

    @Test
    @DisplayName("상품 목록 조회(판매자)")
    public void productListBySellerId() {
        Response resp = given(this.spec)
            .param("userId", 1)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("product-list-by-sellerId",
                    preprocessRequest(modifyUris()
                            .scheme("https")
                            .host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("userId").description("사용자 ID(판매자)")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보"),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("상품 정보"),
                        fieldWithPath("data.content.[].productId").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("data.content.[].productName").type(JsonFieldType.STRING).description("상품 명"),
                        fieldWithPath("data.content.[].sellerId").type(JsonFieldType.NUMBER).description("셀러 아이디"),
                        fieldWithPath("data.content.[].storeName").type(JsonFieldType.STRING).description("셀러 스토어 이름"),
                        fieldWithPath("data.content.[].productStatus").type(JsonFieldType.NUMBER).description("상품 상태"),
                        fieldWithPath("data.content.[].productRemains").type(JsonFieldType.NUMBER).description("상품 재고"),
                        fieldWithPath("data.content.[].productPrice").type(JsonFieldType.NUMBER).description("상품 가격"),
                        fieldWithPath("data.content.[].productCategory").type(JsonFieldType.STRING).description("상품 카테고리"),
                        fieldWithPath("data.content.[].productThumbnailImg").type(JsonFieldType.STRING).description("상품 썸네일"),
                        fieldWithPath("data.content.[].productCreatedAt").type(JsonFieldType.STRING).description("생성일자 (yyyy-MM-dd'T'HH:mm:ss)"),
                        fieldWithPath("data.content.[].productUpdatedAt").type(JsonFieldType.STRING).description("수정일자 (yyyy-MM-dd'T'HH:mm:ss)"),
                        fieldWithPath("data.content.[].productDeletedAt").description("삭제일자 (yyyy-MM-dd'T'HH:mm:ss)").optional(),
                        fieldWithPath("data.content.[].productIsDeleted").type(JsonFieldType.BOOLEAN).description("삭제여부"),
                        fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이징 변수"),
                        fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                        fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보 여부"),
                        fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 처리 여부"),
                        fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 처리 여부"),
                        fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("offset"),
                        fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지당 갯수"),
                        fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징 처리 여부"),
                        fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 처리 여부"),
                        fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("총 항목 수"),
                        fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지당 항목 수"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보 여부"),
                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬처리여부"),
                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬처리여부"),
                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("총 개수"),
                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("데이터 유무 여부")
                    )
                )
            )
            .when()
            .port(port)
            .get("/api/v1/product/seller/list");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("선택된 상품 정보 가져오기")
    public void getProduct() {
        Response resp = given(this.spec)
            .param("productId", 2)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("get-product",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("productId").description("상품 ID")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("반환하는 데이터 정보"),
                    fieldWithPath("data.product").type(JsonFieldType.OBJECT).description("상품 정보"),
                    fieldWithPath("data.product.productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                    fieldWithPath("data.product.productName").type(JsonFieldType.STRING).description("상품 명"),
                    fieldWithPath("data.product.storeName").type(JsonFieldType.STRING).description("판매자 상호 이름"),
                    fieldWithPath("data.product.sellerId").type(JsonFieldType.NUMBER).description("판매자 ID"),
                    fieldWithPath("data.product.storeName").type(JsonFieldType.STRING).description("판매자의 상호 이름"),
                    fieldWithPath("data.product.productStatus").type(JsonFieldType.NUMBER).description("상품의 상태"),
                    fieldWithPath("data.product.productRemains").type(JsonFieldType.NUMBER).description("상품 재고"),
                    fieldWithPath("data.product.productPrice").type(JsonFieldType.NUMBER).description("상품 가격"),
                    fieldWithPath("data.product.productCategory").type(JsonFieldType.STRING).description("상품의 카테고리"),
                    fieldWithPath("data.product.productThumbnailImg").type(JsonFieldType.STRING).description("상품의 썸네일 이미지"),
                    fieldWithPath("data.product.productCreatedAt").type(JsonFieldType.STRING).description("상품의 생성 일자 (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.product.productUpdatedAt").type(JsonFieldType.STRING).description("상품의 수정 일자 (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.product.productDeletedAt").description("상품의 삭제 일자  (yyyy-MM-dd'T'HH:mm:ss").optional(),
                    fieldWithPath("data.product.productIsDeleted").type(JsonFieldType.BOOLEAN).description("상품의 삭제 여부").optional(),
                    fieldWithPath("data.productDetail").type(JsonFieldType.ARRAY).description("상품 이미지 정보"),
                    fieldWithPath("data.productDetail.[].productDetailId").type(JsonFieldType.NUMBER).description("상품 상세 ID"),
                    fieldWithPath("data.productDetail.[].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                    fieldWithPath("data.productDetail.[].img").type(JsonFieldType.STRING).description("상품 이미지"),
                    fieldWithPath("data.productDetail.[].imgCaption").type(JsonFieldType.STRING).description("상품 이미지 상세 설명"),
                    fieldWithPath("data.storeName").type(JsonFieldType.STRING).description("상품을 판매자한 스토어 이름")
                    )
            ))
            .when()
            .port(port)
            .get("/api/v1/product");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("상품 생성")
    public void createProduct() {
        ProductRequestDto p = new ProductRequestDto("테스트", 30L, 500L, "테스트", "img.jpg");
        Long productId = productService.saveProduct(1L, p);

        JSONObject request = new JSONObject();
        request.put("userId", 1);
        request.put("productName", "테스트");
        request.put("productRemains", 30L);
        request.put("productPrice", 500L);
        request.put("productCategory", "테스트");
        request.put("productThumbnailImg", "img.jpg");

        Response resp = given(this.spec)
            .body(request.toJSONString())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("create-product",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("userId").description("판매자 Id"),
                        fieldWithPath("productName").type(JsonFieldType.STRING).description("상품 이름"),
                        fieldWithPath("productRemains").type(JsonFieldType.NUMBER).description("상품 재고"),
                        fieldWithPath("productPrice").type(JsonFieldType.NUMBER).description("상품 가격"),
                        fieldWithPath("productCategory").type(JsonFieldType.STRING).description("상품 카테고리"),
                        fieldWithPath("productThumbnailImg").type(JsonFieldType.STRING).description("상품 썸네일 이미지")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 정보")
                    )
                )
            )
            .when()
            .port(port)
            .post("/api/v1/product");

        // Then
        productRepository.deleteById(productId);
        Assertions.assertEquals(HttpStatus.CREATED.value(), resp.statusCode());
    }

    @Test
    @DisplayName("상품 목록 검색 및 조회")
    public void productList() {
        Response resp = given(this.spec)
            .param("sellerId", 1)
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
                        parameterWithName("sellerId").description("셀러 이름").optional(),
                        parameterWithName("storeName").description("셀러 스토어 이름").optional(),
                        parameterWithName("category").description("카테고리").optional(),
                        parameterWithName("productName").description("상품명").optional()
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보"),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("상품 정보"),
                        fieldWithPath("data.content.[].productId").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("data.content.[].productName").type(JsonFieldType.STRING).description("상품 명"),
                        fieldWithPath("data.content.[].sellerId").type(JsonFieldType.NUMBER).description("셀러 아이디"),
                        fieldWithPath("data.content.[].storeName").type(JsonFieldType.STRING).description("셀러 스토어 이름"),
                        fieldWithPath("data.content.[].productStatus").type(JsonFieldType.NUMBER).description("상품 상태"),
                        fieldWithPath("data.content.[].productRemains").type(JsonFieldType.NUMBER).description("상품 재고"),
                        fieldWithPath("data.content.[].productPrice").type(JsonFieldType.NUMBER).description("상품 가격"),
                        fieldWithPath("data.content.[].productCategory").type(JsonFieldType.STRING).description("상품 카테고리"),
                        fieldWithPath("data.content.[].productThumbnailImg").type(JsonFieldType.STRING).description("상품 썸네일"),
                        fieldWithPath("data.content.[].productCreatedAt").type(JsonFieldType.STRING).description("생성일자 (yyyy-MM-dd'T'HH:mm:ss)"),
                        fieldWithPath("data.content.[].productUpdatedAt").type(JsonFieldType.STRING).description("수정일자 (yyyy-MM-dd'T'HH:mm:ss)"),
                        fieldWithPath("data.content.[].productDeletedAt").description("삭제일자 (yyyy-MM-dd'T'HH:mm:ss)").optional(),
                        fieldWithPath("data.content.[].productIsDeleted").type(JsonFieldType.BOOLEAN).description("삭제여부"),
                        fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이징 변수"),
                        fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                        fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보 여부"),
                        fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 처리 여부"),
                        fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬 처리 여부"),
                        fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("offset"),
                        fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("페이지당 갯수"),
                        fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("페이징 처리 여부"),
                        fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("페이징 처리 여부"),
                        fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("총 항목 수"),
                        fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지당 항목 수"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("정렬 정보 여부"),
                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬처리여부"),
                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("정렬처리여부"),
                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("총 개수"),
                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("첫 페이지 여부"),
                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("데이터 유무 여부")
                    )
                )
            )
            .when()
            .port(port)
            .get("/api/v1/product/list");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

}
