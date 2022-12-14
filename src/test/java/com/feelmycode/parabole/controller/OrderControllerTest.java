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

import com.feelmycode.parabole.dto.OrderInfoRequestListDto;
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
public class OrderControllerTest {

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
    @DisplayName("?????? ??????/??????")
    public void updateOrder() {

        // Given
        UserDto userDto = UserDto.builder().email("1111").password("1111").build();
        String token = getToken(userDto);

        JSONObject request = new JSONObject();
        request.put("orderId", 1);

        List<Long> orderInfoIdList = new ArrayList<>();
        orderInfoIdList.add(1L);
        orderInfoIdList.add(2L);
        orderInfoIdList.add(3L);

        List<OrderInfoRequestListDto> requestListDto = new ArrayList<>();
        requestListDto.add(new OrderInfoRequestListDto(orderInfoIdList, ""));

        request.put("orderInfoRequestList", requestListDto);
        request.put("orderPayState", "NAVER_PAY");
        request.put("receiverName", "?????????");
        request.put("receiverPhone", "010-2345-6789");
        request.put("addressSimple", "?????????");
        request.put("addressDetail", "12-33");
        request.put("deliveryComment", "????????? ?????? ???????????????");

        // When
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .body(request.toJSONString())
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .filter(document("update-order",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("orderId").type(JsonFieldType.NUMBER).description("?????? ID"),
                    fieldWithPath("orderInfoRequestList").type(JsonFieldType.ARRAY).description("??????????????? ????????? ?????? ??????"),
                    fieldWithPath("orderInfoRequestList.[].orderInfoIdList").type(JsonFieldType.ARRAY).description("???????????? ID ?????????"),
                    fieldWithPath("orderInfoRequestList.[].couponSerialNo").type(JsonFieldType.STRING).description("?????? ????????? ??????"),
                    fieldWithPath("receiverName").type(JsonFieldType.STRING).description("???????????? ??????"),
                    fieldWithPath("receiverPhone").type(JsonFieldType.STRING).description("???????????? ????????????"),
                    fieldWithPath("addressSimple").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("addressDetail").type(JsonFieldType.STRING).description("?????? ?????????"),
                    fieldWithPath("deliveryComment").type(JsonFieldType.STRING).description("?????? ?????????"),
                    fieldWithPath("orderPayState").type(JsonFieldType.STRING).description("?????? ?????? ??????. {\'CARD\': \'????????????\', \'BANK_TRANSFER\': \'????????? ?????? ??????\', \'PHONE\': \'????????? ??????\', \'VIRTUAL_ACCOUNT\': \'????????????\', \'KAKAO_PAY\': \'????????? ??????\', \'TOSS\': \'??????\', \'WITHOUT_BANK\': \'????????? ??????\', \'WITHOUT_BANK_PAY\': \'????????? ?????? ?????? ??????\', \'NAVER_PAY\': \'????????? ??????\', \'ERROR\': \'??????\'}")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").type(JsonFieldType.NULL).description("?????? ?????? ??????")
                )
            ))
            .when()
            .port(port)
            .post("/api/v1/order");

        // Then
        // ?????? ????????? ????????? ???????????? ?????? ????????? 400 ERROR
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("?????? ??????")
    public void getOrderList() {

        // given
        UserDto userDto = UserDto.builder().email("1111").password("1111").build();
        String token = getToken(userDto);

        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .filter(document("get-order-list",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ??????"),
                    fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                    fieldWithPath("data.[].state").type(JsonFieldType.STRING).description("?????? ???"),
                    fieldWithPath("data.[].userEmail").type(JsonFieldType.STRING).description("????????? ?????????"),
                    fieldWithPath("data.[].productId").type(JsonFieldType.NUMBER).description("?????? ID"),
                    fieldWithPath("data.[].productName").type(JsonFieldType.STRING).description("?????? ???"),
                    fieldWithPath("data.[].productCnt").type(JsonFieldType.NUMBER).description("?????? ??????"),
                    fieldWithPath("data.[].productRemain").type(JsonFieldType.NUMBER).description("?????? ??????"),
                    fieldWithPath("data.[].productPrice").type(JsonFieldType.NUMBER).description("?????? ??????"),
                    fieldWithPath("data.[].productDiscountPrice").type(JsonFieldType.NUMBER).description("?????? ?????? ??????").optional(),
                    fieldWithPath("data.[].productThumbnailImg").type(JsonFieldType.STRING).description("?????? ????????? ?????????"),
                    fieldWithPath("data.[].updatedAt").type(JsonFieldType.STRING).description("?????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)").optional()
                )
            ))
            .when()
            .port(port)
            .get("/api/v1/order");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

}
