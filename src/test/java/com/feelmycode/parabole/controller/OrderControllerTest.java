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

import com.feelmycode.parabole.dto.OrderInfoRequestListDto;
import com.feelmycode.parabole.global.util.StringUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONObject;
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

        // Given
        JSONObject request = new JSONObject();
        request.put("userId", 3);
        request.put("orderId", 1);

        List<Long> orderInfoIdList = new ArrayList<>();
        orderInfoIdList.add(1L);
        orderInfoIdList.add(2L);
        orderInfoIdList.add(3L);

        List<OrderInfoRequestListDto> requestListDto = new ArrayList<>();
        requestListDto.add(new OrderInfoRequestListDto(orderInfoIdList, ""));

        request.put("orderInfoRequestList", requestListDto);
        request.put("orderPayState", "NAVER_PAY");
        request.put("userName", "김파라");
        request.put("userEmail", "para@bole.com");
        request.put("userPhone", "010-2345-6789");
        request.put("receiverName", "김파라");
        request.put("receiverPhone", "010-2345-6789");
        request.put("addressSimple", "광진구");
        request.put("addressDetail", "12-33");
        request.put("deliveryComment", "문앞에 두고 연락주세요");
        request.put("orderState", "PAY_COMPLETE");
        request.put("orderInfoState", "DELIVERY");

        // When
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .body(request.toJSONString())
            .contentType(ContentType.JSON)
            .filter(document("update-order",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                    fieldWithPath("orderId").type(JsonFieldType.NUMBER).description("주문 ID"),
                    fieldWithPath("orderInfoRequestList").type(JsonFieldType.ARRAY).description("상세주문과 적용한 쿠폰 정보"),
                    fieldWithPath("orderInfoRequestList.[].orderInfoIdList").type(JsonFieldType.ARRAY).description("상세주문 ID 리스트"),
                    fieldWithPath("orderInfoRequestList.[].couponSerialNo").type(JsonFieldType.STRING).description("쿠폰 시리얼 넘버"),
                    fieldWithPath("userName").type(JsonFieldType.STRING).description("사용자 이름"),
                    fieldWithPath("userEmail").type(JsonFieldType.STRING).description("사용자 이메일"),
                    fieldWithPath("userPhone").type(JsonFieldType.STRING).description("사용자 전화번호"),
                    fieldWithPath("receiverName").type(JsonFieldType.STRING).description("받는사람 이름"),
                    fieldWithPath("receiverPhone").type(JsonFieldType.STRING).description("받는사람 전화번호"),
                    fieldWithPath("addressSimple").type(JsonFieldType.STRING).description("주소지"),
                    fieldWithPath("addressDetail").type(JsonFieldType.STRING).description("상세 주소지"),
                    fieldWithPath("deliveryComment").type(JsonFieldType.STRING).description("배송 메세지"),
                    fieldWithPath("orderState").type(JsonFieldType.STRING).description("주문 상태. {\'BEFORE_PAY\': \'주문 확정 전\', \'PAY_COMPLETE\': \'주문 확정\', \'DELIVERY_COMPLETE\': \'모든 배송 완료\', \'ERROR\': \'에러\'}"),
                    fieldWithPath("orderInfoState").type(JsonFieldType.STRING).description("상세주문 상태. {\'BEFORE_PAY\': \'입금 전\', \'PAY_COMPLETE\': \'주문완료(결제)\', \'BEFORE_DELIVERY\': \'배송준비\', \'DELIVERY\': \'배송중\', \'DELIVERY_COMPLETE\': \'배송완료\', \'BEFORE_ORDER\': \'주문 전\', \'ORDER_CANCEL\': \'주문 취소\', \'REFUND\': \'환불\', \'ERROR\': \'에러\'}"),
                    fieldWithPath("orderPayState").type(JsonFieldType.STRING).description("주문 결제 수단. {\'CARD\': \'카드결제\', \'BANK_TRANSFER\': \'실시간 계좌 이체\', \'PHONE\': \'휴대폰 결제\', \'VIRTUAL_ACCOUNT\': \'가상계좌\', \'KAKAO_PAY\': \'카카오 페이\', \'TOSS\': \'토스\', \'WITHOUT_BANK\': \'무통장 입금\', \'WITHOUT_BANK_PAY\': \'무통장 입금 결제 완료\', \'NAVER_PAY\': \'네이버 페이\', \'ERROR\': \'에러\'}")
                ),
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
        // 쿠폰 시리얼 넘버가 존재하지 않기 때문에 400 ERROR
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
                    fieldWithPath("data.[].productDiscountPrice").description("상품 할인 가격"),
                    fieldWithPath("data.[].productThumbnailImg").type(JsonFieldType.STRING).description("상품 썸네일 이미지"),
                    fieldWithPath("data.[].updatedAt").description("주문 생성 일자 (yyyy-MM-dd'T'HH:mm:ss")
                )
            ))
            .when()
            .port(port)
            .get("/api/v1/order");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

}
