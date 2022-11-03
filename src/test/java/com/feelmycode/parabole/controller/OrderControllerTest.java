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

import com.feelmycode.parabole.dto.OrderDeliveryUpdateRequestDto;
import com.feelmycode.parabole.dto.OrderRequestDto;
import com.feelmycode.parabole.global.util.StringUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
public class OrderControllerTest {

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
    @DisplayName("주문 수정")
    public void updateOrder() {
        OrderRequestDto dto = new OrderRequestDto(3L, "NAVER_PAY");

        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .body(dto)
            .contentType(ContentType.JSON)
            .filter(document("update-order",
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
            .post("/api/v1/order");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("배송 정보 수정")
    public void updateDelivery() {
        OrderDeliveryUpdateRequestDto dto = new OrderDeliveryUpdateRequestDto(3L, "김파라", "para@bole.com",
            "010-2345-6789", "김파라", "010-2345-6789", "광진구", "12-33",
            "문앞에 두고 연락주세요", "PAY_COMPLETE", "DELIVERY", "TOSS");

        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .body(dto)
            .contentType(ContentType.JSON)
            .filter(document("update-order-delivery",
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
            .patch("/api/v1/order");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("주문 목록")
    public void getOrderList() {
        Response resp = given(this.spec)
            .param("userId", 3L)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("get-order-list",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("주문 정보"),
                    fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                    fieldWithPath("data.[].state").type(JsonFieldType.STRING).description("상품 명"),
                    fieldWithPath("data.[].userId").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                    fieldWithPath("data.[].userEmail").type(JsonFieldType.STRING).description("사용자 이메일"),
                    fieldWithPath("data.[].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                    fieldWithPath("data.[].productName").type(JsonFieldType.STRING).description("상품 명"),
                    fieldWithPath("data.[].productCnt").type(JsonFieldType.NUMBER).description("상품 개수"),
                    fieldWithPath("data.[].productRemain").type(JsonFieldType.NUMBER).description("상품 재고"),
                    fieldWithPath("data.[].productPrice").type(JsonFieldType.NUMBER).description("상품 가격"),
                    fieldWithPath("data.[].productDiscountPrice").type(JsonFieldType.NUMBER).description("상품 할인 가격"),
                    fieldWithPath("data.[].productThumbnailImg").type(JsonFieldType.STRING).description("상품 썸네일 이미지"),
                    fieldWithPath("data.[].updatedAt").type(JsonFieldType.STRING).description("주문 생성 일자")
                )
            ))
            .when()
            .port(port)
            .get("/api/v1/order");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

}
