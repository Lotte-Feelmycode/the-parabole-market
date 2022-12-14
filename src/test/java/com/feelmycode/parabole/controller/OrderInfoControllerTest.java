package com.feelmycode.parabole.controller;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.feelmycode.parabole.dto.OrderInfoSimpleDto;
import com.feelmycode.parabole.dto.UserDto;
import com.feelmycode.parabole.global.util.JwtUtils;
import com.feelmycode.parabole.global.util.StringUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
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
    @DisplayName("?????? ?????? ??????")
    public void createOrderInfo() {
        UserDto userDto = UserDto.builder().email("thepara@bole.com").password("1234").build();
        String token = getToken(userDto);

        OrderInfoSimpleDto dto = new OrderInfoSimpleDto(2L, 1);
        List<OrderInfoSimpleDto> orderInfoDto = new ArrayList<>();
        orderInfoDto.add(dto);

        JSONObject request = new JSONObject();
        request.put("orderInfoDto", orderInfoDto);
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .body(request.toJSONString())
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .filter(document("create-orderinfo",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("orderInfoDto").type(JsonFieldType.ARRAY).description("?????? ?????? ?????????"),
                    fieldWithPath("orderInfoDto.[].productId").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                    fieldWithPath("orderInfoDto.[].productCnt").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("?????? ?????? ??? ?????????")
                )
            ))
            .when()
            .port(port)
            .post("/api/v1/orderinfo");

        // Then
        Assertions.assertEquals(HttpStatus.CREATED.value(), resp.statusCode());
    }

    @Test
    @DisplayName("?????? ?????? ??????(?????????)")
    public void getOrderInfoList() {

        // given
        UserDto userDto = UserDto.builder().email("1111").password("1111").build();
        String token = getToken(userDto);

        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .filter(document("get-orderinfo-list-by-user",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ??????"),
                    fieldWithPath("data.orderId").type(JsonFieldType.NUMBER).description("?????? ID"),
                    fieldWithPath("data.cnt").type(JsonFieldType.NUMBER).description("????????? ???????????? ??? ??????"),
                    fieldWithPath("data.orderBySellerDtoList").type(JsonFieldType.ARRAY).description("??????????????? ????????? ?????? ??????"),
                    fieldWithPath("data.orderBySellerDtoList.[].sellerId").type(JsonFieldType.NUMBER).description("????????? ID"),
                    fieldWithPath("data.orderBySellerDtoList.[].storeName").type(JsonFieldType.STRING).description("????????? ?????? ???"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos").type(JsonFieldType.ARRAY).description("????????? ?????? ??????"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].id").type(JsonFieldType.NUMBER).description("?????? ?????? ID"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].state").type(JsonFieldType.STRING).description("?????? ?????? ??????. {\'BEFORE_PAY\': \'?????? ???\', \'PAY_COMPLETE\': \'????????????(??????)\', \'BEFORE_DELIVERY\': \'?????? ??????\', \'DELIVERY\': \'?????? ???\', \'DELIVERY_COMPLETE\': \'?????? ??????\', \'BEFORE_ORDER\': \'?????? ???\', \'ORDER_CANCEL\': \'?????? ??????\', \'REFUND\': \'??????\', \'ERROR\': \'??????\'}"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].userEmail").type(JsonFieldType.STRING).description("?????? ?????????"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productId").type(JsonFieldType.NUMBER).description("?????? ID"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productName").type(JsonFieldType.STRING).description("?????? ???"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productCnt").type(JsonFieldType.NUMBER).description("????????? ?????? ??????"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productRemain").description("???????????? ?????? ??????"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productPrice").type(JsonFieldType.NUMBER).description("?????? ??? ??????"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productDiscountPrice").description("?????? ?????? ??????"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].productThumbnailImg").description("?????? ?????????"),
                    fieldWithPath("data.orderBySellerDtoList.[].orderInfoResponseDtos.[].updatedAt").description("?????? ?????? ??????"),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto").type(JsonFieldType.OBJECT).description("?????? ??????(????????? ??????:RATE, ?????? ?????? ??????:AMOUNT)").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.rateCoupon").type(JsonFieldType.ARRAY).description("????????? ?????? ??????").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.rateCoupon.[].couponName").type(JsonFieldType.STRING).description("?????? ??????").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.rateCoupon.[].serialNo").type(JsonFieldType.STRING).description("?????? ????????? ??????").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.rateCoupon.[].storeName").type(JsonFieldType.STRING).description("????????? ????????? ????????? ?????? ???").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.rateCoupon.[].type").type(JsonFieldType.STRING).description("?????? ??????(?????????)").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.rateCoupon.[].discountValue").type(JsonFieldType.NUMBER).description("???????????? ????????????").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.amountCoupon").type(JsonFieldType.ARRAY).description("?????? ?????? ?????? ??????").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.amountCoupon.[].couponName").type(JsonFieldType.STRING).description("?????? ??????").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.amountCoupon.[].serialNo").type(JsonFieldType.STRING).description("?????? ????????? ??????").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.amountCoupon.[].storeName").type(JsonFieldType.STRING).description("????????? ????????? ????????? ?????? ???").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.amountCoupon.[].type").type(JsonFieldType.STRING).description("?????? ??????(??????)").optional(),
                    fieldWithPath("data.orderBySellerDtoList.[].couponDto.amountCoupon.[].discountValue").type(JsonFieldType.NUMBER).description("???????????? ??????").optional()
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
    @DisplayName("?????? ?????? ??????(?????????)")
    public void getOrderInfoListBySeller() {

        // given
        UserDto userDto = UserDto.builder().email("test@test.com").password("test").build();
        String token = getToken(userDto);

        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .filter(document("get-orderinfo-list-by-seller",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ??????").optional(),
                    fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("?????? ???????????? ID"),
                    fieldWithPath("data.[].state").type(JsonFieldType.STRING).description("?????? ??????, {\'BEFORE_PAY\': \'?????? ?????? ???\', \'PAY_COMPLETE\': \'?????? ??????\', \'DELIVERY_COMPLETE\': \'?????? ?????? ??????\'}"),
                    fieldWithPath("data.[].userEmail").type(JsonFieldType.STRING).description("????????? ?????????"),
                    fieldWithPath("data.[].productId").type(JsonFieldType.NUMBER).description("?????? ID"),
                    fieldWithPath("data.[].productName").type(JsonFieldType.STRING).description("?????? ???"),
                    fieldWithPath("data.[].productCnt").type(JsonFieldType.NUMBER).description("?????? ??????"),
                    fieldWithPath("data.[].productRemain").type(JsonFieldType.NUMBER).description("?????? ??????"),
                    fieldWithPath("data.[].productPrice").type(JsonFieldType.NUMBER).description("?????? ??????"),
                    fieldWithPath("data.[].productDiscountPrice").type(JsonFieldType.NUMBER).description("?????? ?????? ??????").optional(),
                    fieldWithPath("data.[].productThumbnailImg").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                    fieldWithPath("data.[].updatedAt").type(JsonFieldType.STRING).description("?????? ???????????? (yyyy-MM-dd'T'HH:mm:ss)").optional()
                )
            ))
            .when()
            .port(port)
            .get("/api/v1/orderinfo/seller");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    public void updateOrderInfo() {

        // given
        UserDto userDto = UserDto.builder().email("test@test.com").password("test").build();
        String token = getToken(userDto);

        JSONObject request = new JSONObject();
        request.put("orderInfoId", 11L);
        request.put("orderState", "DELIVERY");

        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .body(request.toJSONString())
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .filter(document("update-orderinfo",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("orderInfoId").type(JsonFieldType.NUMBER).description("?????? ?????? ID"),
                    fieldWithPath("orderState").type(JsonFieldType.STRING).description("?????? ??????")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("?????? ?????? ??????")
                )
            ))
            .when()
            .port(port)
            .patch("/api/v1/orderinfo");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

}
