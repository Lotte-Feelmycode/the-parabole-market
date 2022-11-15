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
import com.feelmycode.parabole.dto.UserDto;
import com.feelmycode.parabole.global.util.JwtUtils;
import com.feelmycode.parabole.global.util.StringUtil;
import com.feelmycode.parabole.repository.CartItemRepository;
import com.feelmycode.parabole.repository.CartRepository;
import com.feelmycode.parabole.service.CartItemService;
import com.feelmycode.parabole.service.CartService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
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
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(StringUtil.ASCII_DOC_OUTPUT_DIR);

    private RequestSpecification spec;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Before
    public void setUp() {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
            .build();

        // Given
        Optional<CartItem> cartItemOptional = cartItemRepository.findByCartIdAndProductId(3L, 3L);
        cartItemOptional.ifPresent(cartItem -> cartItemRepository.deleteById(cartItem.getId()));
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
    @DisplayName("장바구니 목록 조회")
    public void test01_cartList() {

        // Given
        UserDto userDto = UserDto.builder().email("test@test.com").password("test").build();
        String token = getToken(userDto);

        // When
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .filter(
                document(
                    "cart-list",
                    preprocessRequest(modifyUris().scheme("https").host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보"),
                        fieldWithPath("data.cartId").type(JsonFieldType.NUMBER).description("장바구니 아이디"),
                        fieldWithPath("data.cnt").type(JsonFieldType.NUMBER).description("장바구니 아이디"),
                        fieldWithPath("data.cartBySellerDtoList").type(JsonFieldType.ARRAY).description("장바구니"),
                        fieldWithPath("data.cartBySellerDtoList.[].sellerId").type(JsonFieldType.NUMBER).description("판매자 아이디"),
                        fieldWithPath("data.cartBySellerDtoList.[].storeName").type(JsonFieldType.STRING).description("쇼핑몰 이름"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList").type(JsonFieldType.ARRAY).description("장바구니 항목 목록"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].cartItemId").type(JsonFieldType.NUMBER).description("장바구니 항목 아이디"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product").type(JsonFieldType.OBJECT).description("장바구니 항목 상품 정보"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.productId").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.productName").type(JsonFieldType.STRING).description("상품 이름"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.sellerId").type(JsonFieldType.NUMBER).description("판매자 아이디"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.storeName").type(JsonFieldType.STRING).description("쇼핑몰 이름"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.productStatus").type(JsonFieldType.NUMBER).description("상품 판매 속성 (판매중 : 1)"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.productRemains").type(JsonFieldType.NUMBER).description("상품 재고"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.productPrice").type(JsonFieldType.NUMBER).description("상품 가격"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.productCategory").type(JsonFieldType.STRING).description("상품 카테고리"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.productThumbnailImg").type(JsonFieldType.STRING).description("상품 미리보기 이미지 url"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.productCreatedAt").type(JsonFieldType.STRING).description("상품 등록일자 (yyyy-MM-dd'T'HH:mm:ss)"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.productUpdatedAt").type(JsonFieldType.STRING).description("상품 수정일자 (yyyy-MM-dd'T'HH:mm:ss)"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.productDeletedAt").type(JsonFieldType.STRING).description("상품 삭제일자 (yyyy-MM-dd'T'HH:mm:ss)").optional(),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].product.productIsDeleted").type(JsonFieldType.BOOLEAN).description("상품 삭제여부 (삭제가 아닐경우 false)"),
                        fieldWithPath("data.cartBySellerDtoList.[].cartItemDtoList.[].count").type(JsonFieldType.NUMBER).description("장바구니 구매요청 수량"),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto").type(JsonFieldType.OBJECT).description("사용가능 쿠폰 목록").optional(),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto.rateCoupon").type(JsonFieldType.ARRAY).description("사용가능 할인 정률쿠폰 목록").optional(),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto.rateCoupon.[].couponName").type(JsonFieldType.STRING).description("쿠폰 이름"),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto.rateCoupon.[].serialNo").type(JsonFieldType.STRING).description("쿠폰 번호"),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto.rateCoupon.[].storeName").type(JsonFieldType.STRING).description("쇼핑몰 이름"),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto.rateCoupon.[].type").type(JsonFieldType.STRING).description("쿠폰 종류 (정률쿠폰 : RATE, 정량쿠폰 : AMOUNT)"),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto.rateCoupon.[].discountValue").type(JsonFieldType.NUMBER).description("사용가능 할인 정률쿠폰 목록"),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto.amountCoupon").type(JsonFieldType.ARRAY).description("사용가능 할인 정액쿠폰 목록").optional(),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto.amountCoupon.[].couponName").type(JsonFieldType.STRING).description("쿠폰 이름"),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto.amountCoupon.[].serialNo").type(JsonFieldType.STRING).description("쿠폰 번호"),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto.amountCoupon.[].storeName").type(JsonFieldType.STRING).description("쇼핑몰 이름"),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto.amountCoupon.[].type").type(JsonFieldType.STRING).description("쿠폰 종류 (정률쿠폰 : RATE, 정량쿠폰 : AMOUNT)"),
                        fieldWithPath("data.cartBySellerDtoList.[].couponDto.amountCoupon.[].discountValue").type(JsonFieldType.NUMBER).description("사용가능 할인 정률쿠폰 목록")
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
        UserDto userDto = UserDto.builder().email("1111").password("1111").build();
        String token = getToken(userDto);

        JSONObject request = new JSONObject();
        request.put("productId", 3L);
        request.put("cnt", 3);

        // When
        Response resp = given(this.spec)
            .body(request.toJSONString())
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .filter(
                document(
                    "cart-product-add",
                    preprocessRequest(modifyUris().scheme("https").host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
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
        UserDto userDto = UserDto.builder().email("test@test.com").password("test").build();
        String token = getToken(userDto);
        Long userId = jwtUtils.extractUserId(token);

        JSONObject request = new JSONObject();
        request.put("cartItemId", cartItemRepository.findByCartIdAndProductId(11L, 3L).get().getId());
        request.put("productId", 3L);
        request.put("cnt", 10);

        // When
        Response resp = given(this.spec)
            .body(request.toJSONString())
            .accept(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
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
        UserDto userDto = UserDto.builder().email("1111").password("1111").build();
        String token = getToken(userDto);

//        cartItemService.addItem(userId, new CartAddItemRequestDto(3L, 3));
        Long cartItemId = cartItemRepository.findByCartIdAndProductId(11L, 3L).get().getId();

        // When
        Response resp = given(this.spec)
            .param("cartItemId", cartItemId)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .filter(
                document(
                    "cart-delete",
                    preprocessRequest(modifyUris().scheme("https").host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
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
