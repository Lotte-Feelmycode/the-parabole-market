package com.feelmycode.parabole.controller;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.feelmycode.parabole.domain.WithMockCustomUser;
import com.feelmycode.parabole.dto.CouponAssignRequestDto;
import com.feelmycode.parabole.dto.CouponCreateRequestDto;
import com.feelmycode.parabole.dto.UserDto;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
public class UserControllerTest {

    String outputDirectory = "./src/docs/asciidoc/snippets";

    @LocalServerPort
    int port;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(outputDirectory);

    private RequestSpecification spec;

    @Before
    public void setUp() {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
            .build();
    }

    @Test
    @DisplayName("회원가입")
    public void signup() {

        UserDto dto = UserDto.builder().email("user1@naver.com").password("user1").build();
        // When
        Response resp = given(this.spec)
            .body(dto)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("auth-controller/auth-signup",
                    preprocessRequest(modifyUris()
                            .scheme("https")
                            .host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("userId").description("유저 ID")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보"),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("쿠폰 목록"),
                        fieldWithPath("data.content.[].name").type(JsonFieldType.STRING)
                            .description("쿠폰명"),
                        fieldWithPath("data.content.[].serialNo").type(JsonFieldType.STRING)
                            .description("쿠폰 일련번호"),
                        fieldWithPath("data.content.[].sellerName").type(JsonFieldType.STRING)
                            .description("셀러 이름"),
                        fieldWithPath("data.content.[].type").type(JsonFieldType.STRING)
                            .description("쿠폰 유형"),
                        fieldWithPath("data.content.[].discountValue").type(JsonFieldType.NUMBER)
                            .description("할인 값(비율/금액)"),
                        fieldWithPath("data.content.[].useState").type(JsonFieldType.STRING)
                            .description("사용 여부"),
                        fieldWithPath("data.content.[].useDate").type(JsonFieldType.STRING)
                            .description("사용일자"),
                        fieldWithPath("data.content.[].acquiredDate").type(JsonFieldType.STRING)
                            .description("획득일자"),
                        fieldWithPath("data.content.[].validAt").type(JsonFieldType.STRING)
                            .description("유효시작일자"),
                        fieldWithPath("data.content.[].expiresAt").type(JsonFieldType.STRING)
                            .description("만료일자"),
                        fieldWithPath("data.pageable").type(JsonFieldType.OBJECT).description("페이징 변수"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                            .description("마지막 페이지 여부"),
                        fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                            .description("전체 페이지 갯수"),
                        fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                            .description("총 요청 갯수"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                            .description("페이지 당 출력 갯수"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER)
                            .description("현재 페이지"),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT)
                            .description("정렬"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                            .description("정렬 정보 여부"),
                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
                            .description("정렬 처리 여부"),
                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
                            .description("미정렬 처리 여부"),
                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
                            .description("첫 페이지 여부"),
                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                            .description("총 항목 갯수"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                            .description("마지막 페이지 여부"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지당 항목 수"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                            .description("정렬 정보 여부")
                    )
                )
            )
            .when()
            .port(port)
            .get("/api/v1/coupon/list");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }


    @Test
    @DisplayName("셀러 본인이 발행한 쿠폰 목록 조회")
    @WithMockCustomUser(id = 1, email = "seller1@naver.com", username = "seller1", role = "ROLE_SELLER")
    public void couponListSeller() {

        // When
        Response resp = given(this.spec)
            .param("userId", "1")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("coupon-controller/coupon-list-seller",
                    preprocessRequest(modifyUris()
                            .scheme("https")
                            .host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("userId").description("유저 ID")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보"),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("쿠폰 목록"),
                        fieldWithPath("data.content.[].couponId").type(JsonFieldType.NUMBER)
                            .description("쿠폰 ID"),
                        fieldWithPath("data.content.[].name").type(JsonFieldType.STRING)
                            .description("쿠폰 이름"),
                        fieldWithPath("data.content.[].type").type(JsonFieldType.STRING)
                            .description("쿠폰 유형"),
                        fieldWithPath("data.content.[].discountValue").type(JsonFieldType.NUMBER)
                            .description("할인 값(비율/금액)"),
                        fieldWithPath("data.content.[].createdAt").type(JsonFieldType.STRING)
                            .description("생성일자"),
                        fieldWithPath("data.content.[].validAt").type(JsonFieldType.STRING)
                            .description("유효시작일자"),
                        fieldWithPath("data.content.[].expiresAt").type(JsonFieldType.STRING)
                            .description("만료일자"),
                        fieldWithPath("data.content.[].detail").type(JsonFieldType.STRING)
                            .description("쿠폰 상세 설명"),
                        fieldWithPath("data.content.[].cnt").type(JsonFieldType.NUMBER)
                            .description("쿠폰 발행 수량"),
                        fieldWithPath("data.content.[].remains").type(JsonFieldType.NUMBER)
                            .description("쿠폰 잔여 수량"),
                        fieldWithPath("data.pageable").type(JsonFieldType.STRING).description("페이징 변수"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                            .description("마지막 페이지 여부"),
                        fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                            .description("전체 페이지 갯수"),
                        fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                            .description("총 요청 갯수"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                            .description("페이지 당 출력 갯수"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER)
                            .description("현재 페이지"),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT)
                            .description("정렬"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                            .description("정렬 정보 여부"),
                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
                            .description("정렬 처리 여부"),
                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
                            .description("미정렬 처리 여부"),
                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
                            .description("첫 페이지 여부"),
                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                            .description("총 항목 갯수"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                            .description("마지막 페이지 여부"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("페이지당 항목 수"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("정렬 정보"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                            .description("정렬 정보 여부"),
                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("빈 여부")
                    )
                )
            )
            .when()
            .port(port)
            .get("/api/v1/coupon/list");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("셀러 새로운 쿠폰 발행")
    @WithMockCustomUser(id = 4, email = "seller1@naver.com", username = "seller1", role = "ROLE_SELLER")
    public void addCoupon() {
        BearerAccessToken token = new BearerAccessToken();
//        TODO: token.getUserid 해서 dto 에 넣어줘야할듯?
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime validAt = LocalDateTime.parse("2022-09-16T00:00:00", formatter);
        LocalDateTime expiresAt = LocalDateTime.parse("2022-11-30T00:00:00", formatter);

        CouponCreateRequestDto dto = new CouponCreateRequestDto("크리스마스 기념 쿠폰", 4L, 2, 2000,
            validAt, expiresAt, "행복한 크리스마스 되세요", 5);

        // When
        Response resp = given(this.spec)
            .param("userId", "4")
            .accept(ContentType.JSON)
            .header("authorization", "Bearer " + token)
            .body(dto)
            .contentType(ContentType.JSON)
            .filter(document("coupon-controller/coupon-add-seller",
                    preprocessRequest(modifyUris()
                            .scheme("https")
                            .host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("userId").description("유저 ID")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보"),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("쿠폰 목록"),
                        fieldWithPath("data.content.[].couponName").type(JsonFieldType.NUMBER)
                            .description("쿠폰 ID"),
                        fieldWithPath("data.content.[].sellerName").type(JsonFieldType.STRING)
                            .description("판매자 이름"),
                        fieldWithPath("data.content.[].type").type(JsonFieldType.STRING)
                            .description("쿠폰 유형"),
                        fieldWithPath("data.content.[].cnt").type(JsonFieldType.NUMBER)
                            .description("쿠폰 발행 수량")
                    )
                )
            )
            .when()
            .port(port)
            .get("/api/v1/coupon/new");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("셀러 유저에게 쿠폰을 배정")
    @WithMockCustomUser(id = 4, email = "seller1@naver.com", username = "seller1", role = "ROLE_SELLER")
    public void assignCoupon() {

        BearerAccessToken token = new BearerAccessToken();
        CouponAssignRequestDto dto = new CouponAssignRequestDto(2L, Arrays.asList(36L, 37L));

        Response resp = given(this.spec)
            .param("userId", "4")
            .accept(ContentType.JSON)
            .header("authorization", "Bearer " + token)
            .body(dto)
            .contentType(ContentType.JSON)
            .filter(document("coupon-controller/coupon-assign",
                    preprocessRequest(modifyUris()
                            .scheme("https")
                            .host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestParameters(
                        parameterWithName("userId").description("유저 ID")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보")
                    )
                )
            )
            .when()
            .port(port)
            .get("/api/v1/coupon/assign");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

}

