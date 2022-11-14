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

import com.feelmycode.parabole.dto.UserDto;
import com.feelmycode.parabole.global.util.JwtUtils;
import com.feelmycode.parabole.global.util.StringUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
public class CouponControllerTest {

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
    @DisplayName("사용자가 특정판매자가 판매하는 상품에 대한 쿠폰 조회")
    public void getCouponBySeller() {

        // given
        UserDto userDto = UserDto.builder().email("1111").password("1111").build();
        String token = getToken(userDto);
        Long userId = jwtUtils.extractUserId(token);

        JSONObject request = new JSONObject();
        request.put("sellerId", 1);
        request.put("totalFee", 50000);

        // when
        Response response = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)
            .header("Authorization", "Bearer " + token)
            .body(request.toJSONString())
            .filter(document("get-coupon-by-seller",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("sellerId").type(JsonFieldType.NUMBER).description("조회할 쿠폰의 판매자 ID"),
                    fieldWithPath("totalFee").type(JsonFieldType.NUMBER).description("총 금액")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 정보"),
                    fieldWithPath("data.[].couponName").type(JsonFieldType.STRING).description("쿠폰 이름").optional(),
                    fieldWithPath("data.[].serialNo").type(JsonFieldType.STRING).description("쿠폰 시리얼 넘버").optional(),
                    fieldWithPath("data.[].storeName").type(JsonFieldType.STRING).description("판매자 스토어 이름").optional(),
                    fieldWithPath("data.[].type").type(JsonFieldType.STRING).description("쿠폰 타입(RATE: 할인율, AMOUN: 할인금액)").optional(),
                    fieldWithPath("data.[].discountValue").type(JsonFieldType.NUMBER).description("할인 금액").optional(),
                    fieldWithPath("data.[].totalFee").type(JsonFieldType.NUMBER).description("할인되는 금액").optional()
                )
            ))
            .get("/api/v1/coupon");

        // then
        Assertions.assertEquals(HttpStatus.OK.value(), response.statusCode());
    }

}
