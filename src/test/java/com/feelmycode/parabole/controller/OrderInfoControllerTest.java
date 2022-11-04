package com.feelmycode.parabole.controller;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.feelmycode.parabole.dto.OrderInfoListDto;
import com.feelmycode.parabole.dto.OrderInfoRequestDto;
import com.feelmycode.parabole.dto.OrderInfoSimpleDto;
import com.feelmycode.parabole.global.util.StringUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderInfoControllerTest {

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
    @DisplayName("상세 주문 생성")
    public void createOrderInfo() {

        OrderInfoSimpleDto dto = new OrderInfoSimpleDto(2L, 1);
        List<OrderInfoSimpleDto> orderInfoDto = new ArrayList<>();
        orderInfoDto.add(dto);
        OrderInfoListDto list = new OrderInfoListDto(1L, orderInfoDto);

        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .body(list)
            .contentType(ContentType.JSON)
            .filter(document("create-orderinfo",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("주문 완료 후 데이터")
                )
            ))
            .when()
            .port(port)
            .post("/api/v1/orderinfo");

        // Then
        Assertions.assertEquals(HttpStatus.CREATED.value(), resp.statusCode());
    }

    @Test
    @DisplayName("상세 주문 목록(사용자)")
    public void getOrderInfoList() {
        Response resp = given(this.spec)
            .param("userId", 3L)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("get-orderinfo-list-by-user",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("주문 정보"),
                    fieldWithPath("data.orderId").type(JsonFieldType.NUMBER).description("주문 ID"),
                    fieldWithPath("data.cnt").type(JsonFieldType.NUMBER).description("주문한 상품들의 총 개수"),
                    fieldWithPath("data.orderBySellerDtoList").type(JsonFieldType.ARRAY).description("판매자별로 정렬된 상품 목록"),
                    fieldWithPath("data.orderBySellerDtoList.[].sellerId").type(JsonFieldType.NUMBER).description("판매자 ID"),
                    fieldWithPath("data.orderBySellerDtoList.[].storeName").type(JsonFieldType.STRING).description("판매자 상호 명"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos").type(JsonFieldType.ARRAY).description("주문한 상품 정보"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].id").type(JsonFieldType.NUMBER).description("상세 주문 ID"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].state").type(JsonFieldType.STRING).description("상세 주문 상태"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].userEmail").type(JsonFieldType.STRING).description("유저 이메일"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productName").type(JsonFieldType.STRING).description("상품 명"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productCnt").type(JsonFieldType.NUMBER).description("주문한 상품 개수"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productRemain").description("남아있는 상품 재고"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productPrice").type(JsonFieldType.NUMBER).description("상품 총 가격"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productDiscountPrice").type(JsonFieldType.NUMBER).description("상품 할인 가격"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productThumbnailImg").description("상품 썸네일"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].updatedAt").description("주문 수정 날짜"),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto").type(JsonFieldType.OBJECT).description("쿠폰 정보").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.rateCoupon").type(JsonFieldType.ARRAY).description("할인율 쿠폰 목록").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.rateCoupon.[].couponName").type(JsonFieldType.STRING).description("쿠폰 이름").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.rateCoupon.[].serialNo").type(JsonFieldType.STRING).description("쿠폰 시리얼 넘버").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.rateCoupon.[].storeName").type(JsonFieldType.STRING).description("쿠폰을 발행한 판매자 상호 명").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.rateCoupon.[].type").type(JsonFieldType.STRING).description("쿠폰 타입(할인율)").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.rateCoupon.[].discountValue").type(JsonFieldType.NUMBER).description("할인되는 퍼센티지").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.amountCoupon").type(JsonFieldType.ARRAY).description("금액 할인 쿠폰 목록").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.amountCoupon.[].couponName").type(JsonFieldType.STRING).description("쿠폰 이름").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.amountCoupon.[].serialNo").type(JsonFieldType.STRING).description("쿠폰 시리얼 넘버").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.amountCoupon.[].storeName").type(JsonFieldType.STRING).description("쿠폰을 발행한 판매자 상호 명").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.amountCoupon.[].type").type(JsonFieldType.STRING).description("쿠폰 타입(금액)").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.amountCoupon.[].discountValue").type(JsonFieldType.NUMBER).description("할인되는 금액").optional()
                    )
                )
            )
            .when()
            .port(port)
            .get("/api/v1/orderinfo");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("상세 주문 목록(판매자)")
    public void getOrderInfoListBySeller() {
        Response resp = given(this.spec)
            .param("userId", 1L)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("get-orderinfo-list-by-seller",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("주문 정보"),
                    fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("주문 상세정보 ID"),
                    fieldWithPath("data.[].state").type(JsonFieldType.STRING).description("주문 상태"),
                    fieldWithPath("data.[].userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                    fieldWithPath("data.[].userEmail").type(JsonFieldType.STRING).description("사용자 이메일"),
                    fieldWithPath("data.[].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                    fieldWithPath("data.[].productName").type(JsonFieldType.STRING).description("상품 명"),
                    fieldWithPath("data.[].productCnt").type(JsonFieldType.NUMBER).description("상품 개수"),
                    fieldWithPath("data.[].productRemain").type(JsonFieldType.NUMBER).description("상품 재고"),
                    fieldWithPath("data.[].productPrice").type(JsonFieldType.NUMBER).description("상품 가격"),
                    fieldWithPath("data.[].productDiscountPrice").type(JsonFieldType.NUMBER).description("상품 할인 가격").optional(),
                    fieldWithPath("data.[].productThumbnailImg").type(JsonFieldType.STRING).description("상품 썸네일 이미지"),
                    fieldWithPath("data.[].updatedAt").description("주문 수정일자")
                )
            ))
            .when()
            .port(port)
            .get("/api/v1/orderinfo/seller");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("상세 주문 수정")
    public void updateOrderInfo() {
        OrderInfoRequestDto dto = new OrderInfoRequestDto(3L, 1L, "BEFORE_PAY");

        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .body(dto)
            .contentType(ContentType.JSON)
            .filter(document("update-orderinfo",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("주문 수정 정보")
                )
            ))
            .when()
            .port(port)
            .patch("/api/v1/orderinfo");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

}
