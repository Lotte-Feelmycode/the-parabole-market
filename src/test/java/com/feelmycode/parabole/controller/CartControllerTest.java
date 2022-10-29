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

import com.feelmycode.parabole.domain.CartItem;
import com.feelmycode.parabole.dto.CartAddItemRequestDto;
import com.feelmycode.parabole.repository.CartItemRepository;
import com.feelmycode.parabole.repository.CartRepository;
import com.feelmycode.parabole.service.CartItemService;
import com.feelmycode.parabole.service.CartService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Optional;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CartControllerTest {

    @LocalServerPort
    int port;
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private RequestSpecification spec;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartRepository cartRepository;

    @Before
    public void setUp() {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
            .build();

        // Given
        Optional<CartItem> cartItemOptional = cartItemRepository.findByCartIdAndProductId(3L,
            3L);
        cartItemOptional.ifPresent(cartItem -> cartItemRepository.deleteById(cartItem.getId()));
    }

    @Test
    @DisplayName("장바구니 목록 조회")
    public void test01_cartList() {

        // Given

        // When
        Response resp = given(this.spec)
            .param("userId", "3")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(
                document(
                    "cart-list",
                    preprocessRequest(modifyUris().scheme("https").host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("userId").description("사용자 아이디")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 정보"),
                        fieldWithPath("data.[]").type(JsonFieldType.ARRAY).description("장바구니 목록"),
                        fieldWithPath("data.[].[]").type(JsonFieldType.ARRAY).description("장바구니 목록 정보"),
                        fieldWithPath("data.[].[].sellerId").type(JsonFieldType.NUMBER).description("판매자 아이디"),
                        fieldWithPath("data.[].[].storeName").type(JsonFieldType.STRING).description("스토어 이름"),
                        fieldWithPath("data.[].[].getItemList").type(JsonFieldType.ARRAY).description("상품 목록"),
                        fieldWithPath("data.[].[].getItemList.[].cartItemId").type(JsonFieldType.NUMBER).description(
                            "장바구니 아이템 아이디"),
                        fieldWithPath("data.[].[].getItemList.[].product").type(JsonFieldType.OBJECT).description("상품 정보"),
                        fieldWithPath("data.[].[].getItemList.[].product.productId").type(JsonFieldType.NUMBER).description(
                            "상품 아이디"),
                        fieldWithPath("data.[].[].getItemList.[].product.productName").type(JsonFieldType.STRING).description(
                            "상품 명"),
                        fieldWithPath("data.[].[].getItemList.[].product.sellerId").type(JsonFieldType.NUMBER).description(
                            "셀러 아이디"),
                        fieldWithPath("data.[].[].getItemList.[].product.storeName").type(JsonFieldType.STRING).description(
                            "스토어 이름"),
                        fieldWithPath(
                            "data.[].[].getItemList.[].product.productStatus").type(JsonFieldType.NUMBER).description("상품 상태"),
                        fieldWithPath(
                            "data.[].[].getItemList.[].product.productRemains").type(JsonFieldType.NUMBER).description(
                            "상품 재고"),
                        fieldWithPath("data.[].[].getItemList.[].product.productPrice").type(JsonFieldType.NUMBER).description(
                            "상품 가격"),
                        fieldWithPath(
                            "data.[].[].getItemList.[].product.productCategory").type(JsonFieldType.STRING).description(
                            "상품 카테고리"),
                        fieldWithPath(
                            "data.[].[].getItemList.[].product.productThumbnailImg").type(JsonFieldType.STRING).description(
                            "상품 썸네일"),
                        fieldWithPath(
                            "data.[].[].getItemList.[].product.productCreatedAt").type(JsonFieldType.STRING).description(
                            "생성일자"),
                        fieldWithPath(
                            "data.[].[].getItemList.[].product.productUpdatedAt").type(JsonFieldType.STRING).description(
                            "수정일자"),
                        fieldWithPath(
                            "data.[].[].getItemList.[].product.productDeletedAt").type(JsonFieldType.NULL).description(
                            "삭제일자"),
                        fieldWithPath(
                            "data.[].[].getItemList.[].product.productIsDeleted").type(JsonFieldType.BOOLEAN).description(
                            "삭제여부"),
                        fieldWithPath("data.[].[].getItemList.[].count").type(JsonFieldType.NUMBER).description("상품 개수"),
                        fieldWithPath("data.[].[].couponDto").type(JsonFieldType.OBJECT).description("보유 쿠폰"),
                        fieldWithPath("data.[].[].couponDto.rateCoupon").type(JsonFieldType.ARRAY).description("할인율 적용 쿠폰"),
                        fieldWithPath("data.[].[].couponDto.rateCoupon.[]").type(JsonFieldType.ARRAY).description("쿠폰 정보"),
                        fieldWithPath("data.[].[].couponDto.rateCoupon.[].couponName").type(JsonFieldType.STRING).description(
                            "쿠폰 이름"),
                        fieldWithPath("data.[].[].couponDto.rateCoupon.[].storeName").type(JsonFieldType.STRING).description(
                            "스토어 이름"),
                        fieldWithPath("data.[].[].couponDto.rateCoupon.[].type").type(JsonFieldType.STRING).description(
                            "쿠폰 유형"),
                        fieldWithPath(
                            "data.[].[].couponDto.rateCoupon.[].discountValue").type(JsonFieldType.NUMBER).description("할인 값"),
                        fieldWithPath("data.[].[].couponDto.amountCoupon").type(JsonFieldType.ARRAY).description("할인율 적용 쿠폰"),
                        fieldWithPath(
                            "data.[].[].couponDto.amountCoupon.[]").type(JsonFieldType.ARRAY).description("쿠폰 정보"),
                        fieldWithPath(
                            "data.[].[].couponDto.amountCoupon.[].couponName").type(JsonFieldType.STRING).description("쿠폰 이름"),
                        fieldWithPath("data.[].[].couponDto.amountCoupon.[].storeName").type(JsonFieldType.STRING).description(
                            "스토어 이름"),
                        fieldWithPath("data.[].[].couponDto.amountCoupon.[].type").type(JsonFieldType.STRING).description(
                            "쿠폰 유형"),
                        fieldWithPath(
                            "data.[].[].couponDto.amountCoupon.[].discountValue").type(JsonFieldType.NUMBER).description(
                            "할인 값")
                    )
                )
            )
            .when()
            .port(port)
            .get("/api/v1/cart/list");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("장바구니 상품 추가")
    public void test02_addProductInCart() {

        // Given
        JSONObject request = new JSONObject();
        request.put("userId", 3L);
        request.put("productId", 3L);
        request.put("cnt", 3);

        // When
        Response resp = given(this.spec)
            .body(request.toJSONString())
            .contentType(ContentType.JSON)
            .filter(
                document(
                    "cart-product-add",
                    preprocessRequest(modifyUris().scheme("https").host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("cnt").type(JsonFieldType.NUMBER).description("수량")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 정보")
                    )
                )
            )
            .when()
            .port(port)
            .post("/api/v1/cart/product/add");

        // Then
        Assertions.assertEquals(HttpStatus.CREATED.value(), resp.statusCode());
    }

    @Test
    @DisplayName("장바구니 상품 수량 변경")
    public void test03_updateProductCnt() {

        // Given
        cartItemService.addItem(new CartAddItemRequestDto(3L, 3L, 3));
        JSONObject request = new JSONObject();
        request.put("cartItemId", cartItemRepository.findByCartIdAndProductId(3L,
            3L).get().getId());
        request.put("productId", 3L);
        request.put("userId", 3L);
        request.put("cnt", 10);

        // When
        Response resp = given(this.spec)
            .body(request.toJSONString())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(
                document(
                    "cart-update-cnt",
                    preprocessRequest(modifyUris().scheme("https").host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("cartItemId").type(JsonFieldType.NUMBER).description("장바구니 아이템 아이디"),
                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                        fieldWithPath("cnt").type(JsonFieldType.NUMBER).description("수량")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 정보")
                    )
                )
            )
            .when()
            .port(port)
            .patch("/api/v1/cart/update/cnt");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("장바구니 상품 제거")
    public void test04_deleteProductInCart() {

        // Given
        cartItemService.addItem(new CartAddItemRequestDto(3L, 3L, 3));

        // When
        Response resp = given(this.spec)
            .param("userId", 3L)
            .param("cartItemId", cartItemRepository.findByCartIdAndProductId(3L,
                3L).get().getId())
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(
                document(
                    "cart-delete",
                    preprocessRequest(modifyUris().scheme("https").host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("userId").description("사용자 아이디"),
                        parameterWithName("cartItemId").description("장바구니 상품 아이디")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 정보")
                    )
                )
            )
            .when()
            .port(port)
            .delete("/api/v1/cart/delete");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }
}
