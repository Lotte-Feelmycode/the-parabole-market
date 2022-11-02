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

import com.feelmycode.parabole.dto.OrderInfoListDto;
import com.feelmycode.parabole.dto.OrderInfoRequestDto;
import com.feelmycode.parabole.dto.OrderInfoSimpleDto;
import com.feelmycode.parabole.global.util.StringUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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

        OrderInfoSimpleDto dto = new OrderInfoSimpleDto(1L, 1);
        List<OrderInfoSimpleDto> orderInfoDto = new ArrayList<>();
        orderInfoDto.add(dto);
        OrderInfoListDto list = new OrderInfoListDto(1L, orderInfoDto);

        given(this.spec)
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
    }

    @Test
    @DisplayName("상세 주문 목록")
    public void getOrderInfoList() {
        given(this.spec)
            .param("userId", 1L)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("get-orderinfo-list",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("주문 정보"),
                    fieldWithPath("data.orderId").type(JsonFieldType.NUMBER).description("주문 ID"),
                    fieldWithPath("data.cnt").type(JsonFieldType.NUMBER).description("총 주문한 상품의 개수"),
                    fieldWithPath("data.[].id").type(JsonFieldType.STRING).description("주문 상세정보 ID"),
                    fieldWithPath("data.[].state").type(JsonFieldType.STRING).description("주문 상태"),
                    fieldWithPath("data.[].userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                    fieldWithPath("data.[].userEmail").type(JsonFieldType.STRING).description("사용자 이메일"),
                    fieldWithPath("data.[].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                    fieldWithPath("data.[].productName").type(JsonFieldType.STRING).description("상품 명"),
                    fieldWithPath("data.[].productCnt").type(JsonFieldType.NUMBER).description("상품 개수"),
                    fieldWithPath("data.[].productRemain").type(JsonFieldType.NUMBER).description("상품 재고"),
                    fieldWithPath("data.[].productPrice").type(JsonFieldType.NUMBER).description("상품 가격"),
                    fieldWithPath("data.[].productDiscountPrice").type(JsonFieldType.NUMBER).description("상품 할인 가격"),
                    fieldWithPath("data.[].productThumbnailImg").type(JsonFieldType.STRING).description("상품 썸네일 이미지"),
                    fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("주문 생성일자"),
                    fieldWithPath("data.couponList").type(JsonFieldType.STRING).description("주문 생성일자"),
                    fieldWithPath("data.couponList.[].createdAt").type(JsonFieldType.STRING).description("주문 생성일자"),
                    fieldWithPath("data.couponList.[].rateCoupon").type(JsonFieldType.ARRAY).description("할인율 쿠폰 목록"),
//                    fieldWithPath("data.couponList.[].rateCoupon.[].couponName").type(JsonFieldType.STRING).description("쿠폰 이름"),
//                    fieldWithPath("data.couponList.[].rateCoupon.[].storeName").type(JsonFieldType.STRING).description("쿠폰 발행 스토어 이름"),
//                    fieldWithPath("data.couponList.[].rateCoupon.[].type").type(JsonFieldType.STRING).description("쿠폰 타입(할인율)"),
//                    fieldWithPath("data.couponList.[].rateCoupon.[].discountValue").type(JsonFieldType.NUMBER).description("할인 퍼센티지"),
                    fieldWithPath("data.couponList.[].amountCoupon").type(JsonFieldType.ARRAY).description("주문 생성일자")/*,*/
//                    fieldWithPath("data.couponList.[].amountCoupon.[].couponName").type(JsonFieldType.STRING).description("쿠폰 이름"),
//                    fieldWithPath("data.couponList.[].amountCoupon.[].storeName").type(JsonFieldType.STRING).description("쿠폰 발행 스토어 이름"),
//                    fieldWithPath("data.couponList.[].amountCoupon.[].type").type(JsonFieldType.STRING).description("쿠폰 타입(금액)"),
//                    fieldWithPath("data.couponList.[].amountCoupon.[].discountValue").type(JsonFieldType.NUMBER).description("할인 금액")


                )
            ))
            .when()
            .port(port)
            .get("/api/v1/orderinfo/seller");
    }

    @Test
    @DisplayName("상세 주문 수정")
    public void updateOrderInfo() {
        OrderInfoRequestDto dto = new OrderInfoRequestDto(3L, 1L, "BEFORE_PAY");
        given(this.spec)
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
    }

}
