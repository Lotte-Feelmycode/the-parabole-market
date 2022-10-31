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

import com.feelmycode.parabole.dto.OrderDeliveryUpdateRequestDto;
import com.feelmycode.parabole.dto.OrderUpdateRequestDto;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderControllerTest {

    @LocalServerPort
    int port;
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

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
        OrderUpdateRequestDto dto = new OrderUpdateRequestDto(1L, "NAVER_PAY");
        given(this.spec)
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
                    fieldWithPath("success").description("성공여부"),
                    fieldWithPath("message").description("메세지"),
                    fieldWithPath("data").description("주문 수정 정보")
                )
            ))
            .when()
            .port(port)
            .post("/api/v1/order");
    }

    @Test
    @DisplayName("배송 정보 수정")
    public void updateDelivery() {
        OrderDeliveryUpdateRequestDto dto = new OrderDeliveryUpdateRequestDto(1L, "김파라", "para@bole.com",
            "010-2345-6789", "김파라", "010-2345-6789", "광진구", "12-33",
            "문앞에 두고 연락주세요", "PAY_COMPLETE", "DELIVERY", "TOSS");
        given(this.spec)
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
                    fieldWithPath("success").description("성공여부"),
                    fieldWithPath("message").description("메세지"),
                    fieldWithPath("data").description("주문 수정 정보")
                )
            ))
            .when()
            .port(port)
            .patch("/api/v1/order");
    }

    @Test
    @DisplayName("주문 목록")
    public void getOrderList() {
        given(this.spec)
            .param("userId", 1L)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("get-order-list",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("userId").description("사용자 ID")
                ),
                responseFields(
                    fieldWithPath("success").description("성공여부"),
                    fieldWithPath("message").description("메세지"),
                    fieldWithPath("data").description("주문 정보"),
                    fieldWithPath("data.[].id").description("상품 아이디"),
                    fieldWithPath("data.[].state").description("상품 명"),
                    fieldWithPath("data.[].userId").description("사용자 아이디"),
                    fieldWithPath("data.[].userEmail").description("사용자 이메일"),
                    fieldWithPath("data.[].productId").description("상품 ID"),
                    fieldWithPath("data.[].productName").description("상품 명"),
                    fieldWithPath("data.[].productCnt").description("상품 개수"),
                    fieldWithPath("data.[].productRemain").description("상품 재고"),
                    fieldWithPath("data.[].productPrice").description("상품 가격"),
                    fieldWithPath("data.[].productDiscountPrice").description("상품 할인 가격"),
                    fieldWithPath("data.[].productThumbnailImg").description("상품 썸네일 이미지")
                )
            ))
            .when()
            .port(port)
            .get("/api/v1/order");
    }

}
