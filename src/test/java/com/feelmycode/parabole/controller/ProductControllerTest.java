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
import com.feelmycode.parabole.dto.UserDto;
import com.feelmycode.parabole.global.util.JwtUtils;
import com.feelmycode.parabole.global.util.StringUtil;
import com.feelmycode.parabole.repository.ProductRepository;
import com.feelmycode.parabole.service.ProductService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
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

    @Autowired
    private JwtUtils jwtUtils;

    @Before
    public void setUp() {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
            .build();
    }

    private String getToken(final UserDto request) {
        final ExtractableResponse<Response> response = given()
            .contentType(ContentType.JSON).body(request)
            .when()
            .post("/api/v1/auth/signin")
            .then()
            .extract();
        return response.body().jsonPath().get("data.token").toString();
    }

    @Test
    @DisplayName("?????? ?????? ??????(?????????)")
    public void productListBySellerId() {

        // given
        UserDto userDto = UserDto.builder().email("test@test.com").password("test").build();
        String token = getToken(userDto);
        Long userId = jwtUtils.extractUserId(token);

        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .filter(document("product-list-by-sellerId",
                    preprocessRequest(modifyUris()
                            .scheme("https")
                            .host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ??????").optional(),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("?????? ??????").optional(),
                        fieldWithPath("data.content.[].productId").type(JsonFieldType.NUMBER).description("?????? ?????????").optional(),
                        fieldWithPath("data.content.[].productName").type(JsonFieldType.STRING).description("?????? ???").optional(),
                        fieldWithPath("data.content.[].sellerId").type(JsonFieldType.NUMBER).description("?????? ?????????").optional(),
                        fieldWithPath("data.content.[].storeName").type(JsonFieldType.STRING).description("?????? ????????? ??????").optional(),
                        fieldWithPath("data.content.[].productStatus").type(JsonFieldType.NUMBER).description("?????? ??????").optional(),
                        fieldWithPath("data.content.[].productRemains").type(JsonFieldType.NUMBER).description("?????? ??????").optional(),
                        fieldWithPath("data.content.[].productPrice").type(JsonFieldType.NUMBER).description("?????? ??????").optional(),
                        fieldWithPath("data.content.[].productCategory").type(JsonFieldType.STRING).description("?????? ????????????").optional(),
                        fieldWithPath("data.content.[].productThumbnailImg").type(JsonFieldType.STRING).description("?????? ?????????").optional(),
                        fieldWithPath("data.content.[].productCreatedAt").type(JsonFieldType.STRING).description("???????????? (yyyy-MM-dd'T'HH:mm:ss)").optional(),
                        fieldWithPath("data.content.[].productUpdatedAt").type(JsonFieldType.STRING).description("???????????? (yyyy-MM-dd'T'HH:mm:ss)").optional(),
                        fieldWithPath("data.content.[].productDeletedAt").type(JsonFieldType.STRING).description("???????????? (yyyy-MM-dd'T'HH:mm:ss)").optional(),
                        fieldWithPath("data.content.[].productIsDeleted").type(JsonFieldType.BOOLEAN).description("????????????").optional(),
                        fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("????????? ??????").optional(),
                        fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT).description("?????? ??????").optional(),
                        fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????").optional(),
                        fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????").optional(),
                        fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????").optional(),
                        fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("offset").optional(),
                        fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("?????? ?????????").optional(),
                        fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("???????????? ??????").optional(),
                        fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("????????? ?????? ??????").optional(),
                        fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("????????? ?????? ??????").optional(),
                        fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("??? ?????? ???").optional(),
                        fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("??? ????????? ???").optional(),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("????????? ????????? ??????").optional(),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("???????????? ?????? ???").optional(),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("?????? ?????????").optional(),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("?????? ??????").optional(),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????").optional(),
                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("??????????????????").optional(),
                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("??????????????????").optional(),
                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("??? ??????").optional(),
                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("??? ????????? ??????").optional(),
                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("????????? ?????? ??????").optional()
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
    @DisplayName("????????? ?????? ?????? ????????????")
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
                    parameterWithName("productId").description("?????? ID")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("???????????? ????????? ??????"),
                    fieldWithPath("data.product").type(JsonFieldType.OBJECT).description("?????? ??????"),
                    fieldWithPath("data.product.productId").type(JsonFieldType.NUMBER).description("?????? ID"),
                    fieldWithPath("data.product.productName").type(JsonFieldType.STRING).description("?????? ???"),
                    fieldWithPath("data.product.storeName").type(JsonFieldType.STRING).description("????????? ?????? ??????"),
                    fieldWithPath("data.product.sellerId").type(JsonFieldType.NUMBER).description("????????? ID"),
                    fieldWithPath("data.product.storeName").type(JsonFieldType.STRING).description("???????????? ?????? ??????"),
                    fieldWithPath("data.product.productStatus").type(JsonFieldType.NUMBER).description("????????? ??????"),
                    fieldWithPath("data.product.productRemains").type(JsonFieldType.NUMBER).description("?????? ??????"),
                    fieldWithPath("data.product.productPrice").type(JsonFieldType.NUMBER).description("?????? ??????"),
                    fieldWithPath("data.product.productCategory").type(JsonFieldType.STRING).description("????????? ????????????"),
                    fieldWithPath("data.product.productThumbnailImg").type(JsonFieldType.STRING).description("????????? ????????? ?????????"),
                    fieldWithPath("data.product.productCreatedAt").type(JsonFieldType.STRING).description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.product.productUpdatedAt").type(JsonFieldType.STRING).description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.product.productDeletedAt").description("????????? ?????? ??????  (yyyy-MM-dd'T'HH:mm:ss").optional(),
                    fieldWithPath("data.product.productIsDeleted").type(JsonFieldType.BOOLEAN).description("????????? ?????? ??????").optional(),
                    fieldWithPath("data.productDetail").type(JsonFieldType.ARRAY).description("?????? ????????? ??????"),
                    fieldWithPath("data.productDetail.[].productDetailId").type(JsonFieldType.NUMBER).description("?????? ?????? ID"),
                    fieldWithPath("data.productDetail.[].productId").type(JsonFieldType.NUMBER).description("?????? ID"),
                    fieldWithPath("data.productDetail.[].img").type(JsonFieldType.STRING).description("?????? ?????????"),
                    fieldWithPath("data.productDetail.[].imgCaption").type(JsonFieldType.STRING).description("?????? ????????? ?????? ??????"),
                    fieldWithPath("data.storeName").type(JsonFieldType.STRING).description("????????? ???????????? ????????? ??????")
                    )
            ))
            .when()
            .port(port)
            .get("/api/v1/product");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("?????? ??????")
    public void createProduct() {
        // given
        UserDto userDto = UserDto.builder().email("test@test.com").password("test").build();
        String token = getToken(userDto);
        Long userId = jwtUtils.extractUserId(token);

        ProductRequestDto p = new ProductRequestDto("?????????", 30L, 500L, "?????????", "img.jpg");
        Long productId = productService.saveProduct(userId, p);

        JSONObject request = new JSONObject();
        request.put("productName", "?????????");
        request.put("productRemains", 30L);
        request.put("productPrice", 500L);
        request.put("productCategory", "?????????");
        request.put("productThumbnailImg", "img.jpg");

        Response resp = given(this.spec)
            .body(request.toJSONString())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .filter(document("create-product",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("productName").type(JsonFieldType.STRING).description("?????? ??????"),
                        fieldWithPath("productRemains").type(JsonFieldType.NUMBER).description("?????? ??????"),
                        fieldWithPath("productPrice").type(JsonFieldType.NUMBER).description("?????? ??????"),
                        fieldWithPath("productCategory").type(JsonFieldType.STRING).description("?????? ????????????"),
                        fieldWithPath("productThumbnailImg").type(JsonFieldType.STRING).description("?????? ????????? ?????????")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER).description("?????? ??????").optional()
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
    @DisplayName("?????? ?????? ?????? ??? ??????")
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
                        parameterWithName("sellerId").description("?????? ??????").optional(),
                        parameterWithName("storeName").description("?????? ????????? ??????").optional(),
                        parameterWithName("category").description("????????????").optional(),
                        parameterWithName("productName").description("?????????").optional()
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ??????"),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("?????? ??????"),
                        fieldWithPath("data.content.[].productId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("data.content.[].productName").type(JsonFieldType.STRING).description("?????? ???"),
                        fieldWithPath("data.content.[].sellerId").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("data.content.[].storeName").type(JsonFieldType.STRING).description("?????? ????????? ??????"),
                        fieldWithPath("data.content.[].productStatus").type(JsonFieldType.NUMBER).description("?????? ??????"),
                        fieldWithPath("data.content.[].productRemains").type(JsonFieldType.NUMBER).description("?????? ??????"),
                        fieldWithPath("data.content.[].productPrice").type(JsonFieldType.NUMBER).description("?????? ??????"),
                        fieldWithPath("data.content.[].productCategory").type(JsonFieldType.STRING).description("?????? ????????????"),
                        fieldWithPath("data.content.[].productThumbnailImg").type(JsonFieldType.STRING).description("?????? ?????????"),
                        fieldWithPath("data.content.[].productCreatedAt").type(JsonFieldType.STRING).description("???????????? (yyyy-MM-dd'T'HH:mm:ss)"),
                        fieldWithPath("data.content.[].productUpdatedAt").type(JsonFieldType.STRING).description("???????????? (yyyy-MM-dd'T'HH:mm:ss)"),
                        fieldWithPath("data.content.[].productDeletedAt").description("???????????? (yyyy-MM-dd'T'HH:mm:ss)").optional(),
                        fieldWithPath("data.content.[].productIsDeleted").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("????????? ??????"),
                        fieldWithPath("data.pageable.sort").type(JsonFieldType.OBJECT).description("?????? ??????"),
                        fieldWithPath("data.pageable.sort.empty").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                        fieldWithPath("data.pageable.sort.sorted").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                        fieldWithPath("data.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                        fieldWithPath("data.pageable.offset").type(JsonFieldType.NUMBER).description("offset"),
                        fieldWithPath("data.pageable.pageNumber").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("data.pageable.pageSize").type(JsonFieldType.NUMBER).description("???????????? ??????"),
                        fieldWithPath("data.pageable.unpaged").type(JsonFieldType.BOOLEAN).description("????????? ?????? ??????"),
                        fieldWithPath("data.pageable.paged").type(JsonFieldType.BOOLEAN).description("????????? ?????? ??????"),
                        fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER).description("??? ?????? ???"),
                        fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER).description("??? ????????? ???"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("????????? ????????? ??????"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("???????????? ?????? ???"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("?????? ??????"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN).description("?????? ?????? ??????"),
                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN).description("??????????????????"),
                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN).description("??????????????????"),
                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER).description("??? ??????"),
                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN).description("??? ????????? ??????"),
                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("????????? ?????? ??????")
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
