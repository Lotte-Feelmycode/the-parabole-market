package com.feelmycode.parabole.controller;

import static io.restassured.RestAssured.given;
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

import com.feelmycode.parabole.repository.CartItemRepository;
import com.feelmycode.parabole.repository.CartRepository;
import com.feelmycode.parabole.service.CartItemService;
import com.feelmycode.parabole.service.CartService;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
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
    }

    @Test
    @DisplayName("장바구니 목록 조회")
    public void cartList() {
        given(this.spec)
            .param("userId","3")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(
                document(
                    "cart-list",
                    preprocessRequest(modifyUris().scheme("https").host("parabole.com"), prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("userId").description("사용자 아이디")
                    ),
                    responseFields(
                        fieldWithPath("success").description("성공여부"),
                        fieldWithPath("message").description("메시지"),
                        fieldWithPath("data").description("응답 정보"),
                        fieldWithPath("data.[].[]").description("장바구니 목록"),
                        fieldWithPath("data.[].[].sellerId").description("판매자 아이디"),
                        fieldWithPath("data.[].[].storeName").description("스토어 이름"),
                        fieldWithPath("data.[].[].getItemList").description("상품 목록"),
                        fieldWithPath("data.[].[].getItemList.[].cartItemId").description("장바구니 아이템 아이디"),
                        fieldWithPath("data.[].[].getItemList.[].product").description("상품 정보"),
                        fieldWithPath("data.[].[].getItemList.[].product.productId").description("상품 아이디"),
                        fieldWithPath("data.[].[].getItemList.[].product.productName").description("상품 명"),
                        fieldWithPath("data.[].[].getItemList.[].product.sellerId").description("셀러 아이디"),
                        fieldWithPath("data.[].[].getItemList.[].product.storeName").description("스토어 이름"),
                        fieldWithPath("data.[].[].getItemList.[].product.productStatus").description("상품 상태"),
                        fieldWithPath("data.[].[].getItemList.[].product.productRemains").description("상품 재고"),
                        fieldWithPath("data.[].[].getItemList.[].product.productPrice").description("상품 가격"),
                        fieldWithPath("data.[].[].getItemList.[].product.productCategory").description("상품 카테고리"),
                        fieldWithPath("data.[].[].getItemList.[].product.productThumbnailImg").description("상품 썸네일"),
                        fieldWithPath("data.[].[].getItemList.[].product.productCreatedAt").description("생성일자"),
                        fieldWithPath("data.[].[].getItemList.[].product.productUpdatedAt").description("수정일자"),
                        fieldWithPath("data.[].[].getItemList.[].product.productDeletedAt").description("삭제일자"),
                        fieldWithPath("data.[].[].getItemList.[].product.productIsDeleted").description("삭제여부"),
                        fieldWithPath("data.[].[].getItemList.[].count").description("상품 개수"),
                        fieldWithPath("data.[].[].couponDto").description("보유 쿠폰"),
                        fieldWithPath("data.[].[].couponDto.rateCoupon").description("할인율 적용 쿠폰"),
                        fieldWithPath("data.[].[].couponDto.rateCoupon.[].couponName").description("쿠폰 이름"),
                        fieldWithPath("data.[].[].couponDto.rateCoupon.[].storeName").description("스토어 이름"),
                        fieldWithPath("data.[].[].couponDto.rateCoupon.[].type").description("쿠폰 유형"),
                        fieldWithPath("data.[].[].couponDto.rateCoupon.[].discountValue").description("할인 값"),
                        fieldWithPath("data.[].[].couponDto.amountCoupon").description("할인율 적용 쿠폰"),
                        fieldWithPath("data.[].[].couponDto.amountCoupon.[].couponName").description("쿠폰 이름"),
                        fieldWithPath("data.[].[].couponDto.amountCoupon.[].storeName").description("스토어 이름"),
                        fieldWithPath("data.[].[].couponDto.amountCoupon.[].type").description("쿠폰 유형"),
                        fieldWithPath("data.[].[].couponDto.amountCoupon.[].discountValue").description("할인 값")
                    )
                )
            )
            .when()
            .port(port)
            .get("/api/v1/cart/list");
    }

    @Test
    @DisplayName("장바구니 상품 추가")
    public void addProductInCart() {
    }

    @Test
    @DisplayName("장바구니 상품 제거")
    public void deleteProductInCart() {
    }

    @Test
    @DisplayName("장바구니 상품 수량 변경")
    public void updateProductCnt() {
    }
}
