package com.feelmycode.parabole.controller;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.feelmycode.parabole.repository.EventParticipantRepository;
import com.feelmycode.parabole.service.EventParticipantService;
import groovy.util.logging.Slf4j;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import javax.transaction.Transactional;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
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
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventApplyControllerTest {

    String outputDirectory = "./src/docs/asciidoc/snippets";
    @LocalServerPort
    int port;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(outputDirectory);

    private RequestSpecification spec;

    @Autowired
    EventParticipantRepository eventParticipantRepository;

    @Autowired
    EventParticipantService eventParticipantService;


    @Before
    public void setUp() {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
            .build();


    }

    @Test
    @DisplayName("이벤트 응모")
    public void test_insertEventApply() {
        Long userId = 6L;
        Long eventId = 2L;
        //given
        JSONObject request = new JSONObject();
        request.put("userId", userId);
        request.put("eventId", eventId);
        request.put("eventPrizeId", 1L);

        Response resp = given(this.spec)
            .body(request.toJSONString())
            .contentType(ContentType.JSON)
            .filter(
                document(
                    "eventparticipant",
                    preprocessRequest(modifyUris().scheme("http").host("parabole.com"),
                        prettyPrint()),
                    requestFields(
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                        fieldWithPath("eventId").type(JsonFieldType.NUMBER).description("이벤트 아이디"),
                        fieldWithPath("eventPrizeId").type(JsonFieldType.NUMBER)
                            .description("이벤트 상품")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 정보")
                    )
                )
            ).when().port(port)
            .post("/api/v1/event/participant");

        //then
        assertEquals(HttpStatus.CREATED.value(), resp.statusCode());
        eventParticipantRepository.deleteByUserIdAndEventId(userId, eventId);
    }

    @Test
    @DisplayName("이벤트 응모 여부")
    public void test1_eventApplyCheck() {
        Long userId = 4L;
        Long eventId = 2L;
        JSONObject request = new JSONObject();
        request.put("userId", userId);
        request.put("eventId", eventId);

        Response resp = given(this.spec)
            .body(request.toJSONString())
            .contentType(ContentType.JSON)
            .filter(
                document(
                    "eventparticipant-check",
                    preprocessRequest(modifyUris().scheme("http").host("parabole.com"),
                        prettyPrint()),
                    requestFields(
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("사용자 아이디"),
                        fieldWithPath("eventId").type(JsonFieldType.NUMBER).description("이벤트 아이디")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                        fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("응답 정보")
                    )
                )
            ).when().port(port)
            .post("/api/v1/event/participant/check");

        //then

        assertEquals(HttpStatus.ALREADY_REPORTED.value(), resp.statusCode());
    }

    @Test
    @DisplayName("유저 이벤트 응모 리스트 조회")
    public void test02_getUserEventParticipants() {

        Long userId = 3L;

        Response resp = given(this.spec)

            .filter(document(
                    "user-eventparticipant",
                    preprocessRequest(modifyUris().scheme("http").host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 정보"),
                        fieldWithPath("data.[].userId").type(JsonFieldType.NUMBER).description("유저 번호"),
                        fieldWithPath("data.[].eventId").type(JsonFieldType.NUMBER)
                            .description("이벤트 번호"),
                        fieldWithPath("data.[].eventTimeStartAt").type(JsonFieldType.STRING)
                            .description("이벤트 시작시간"),
                        fieldWithPath("data.[].eventTitle").type(JsonFieldType.STRING)
                            .description("이벤트 제목"),
                        fieldWithPath("data.[].startAt").type(JsonFieldType.STRING)
                            .description("이벤트 시작시간"),
                        fieldWithPath("data.[].endAt").type(JsonFieldType.STRING)
                            .description("이벤트 종료시간"),
                        fieldWithPath("data.[].status").type(JsonFieldType.NUMBER)
                            .description("이벤트 상태"),
                        fieldWithPath("data.[].eventImg").type(JsonFieldType.STRING)
                            .description("이벤트 이미지")
                    )
                )
            ).when().port(port)
            .get("/api/v1/event/user/participant" + "/{userId}", userId);

        assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("셀러용 이벤트 응모 리스트 조회")
    public void test_getEventParticipants() {

        Long eventId = 2L;

        Response resp = given(this.spec)

            .filter(document(
                    "seller-eventparticipant",
                    preprocessRequest(modifyUris().scheme("http").host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메시지"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 정보"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("응모 번호"),
                        fieldWithPath("data.[].user").type(JsonFieldType.OBJECT)
                            .description("유저 정보"),
                        fieldWithPath("data.[].user.id").type(JsonFieldType.NUMBER)
                            .description("유저 아이디"),
                        fieldWithPath("data.[].user.email").type(JsonFieldType.STRING)
                            .description("유저 이메일"),
                        fieldWithPath("data.[].user.name").type(JsonFieldType.STRING)
                            .description("유저 이름"),
                        fieldWithPath("data.[].user.nickname").type(JsonFieldType.STRING)
                            .description("유저 별명"),
                        fieldWithPath("data.[].user.phone").type(JsonFieldType.STRING)
                            .description("유저 핸드폰 번호"),
                        fieldWithPath("data.[].eventPrizes.[]").type(JsonFieldType.ARRAY)
                            .description("유저 응모 상품 정보"),
                        fieldWithPath("data.[].eventPrizes.[].eventPrizeId").type(JsonFieldType.NUMBER)
                            .optional()
                            .description("이벤트 상품 아이디"),
                        fieldWithPath("data.[].eventPrizes.[].prizeType").type(JsonFieldType.STRING)
                            .optional()
                            .description("이벤트 상품 타입"),
                        fieldWithPath("data.[].eventPrizes.[].stock").type(JsonFieldType.NUMBER)
                            .optional()
                            .description("이벤트 상품 재고"),
                        fieldWithPath("data.[].eventPrizes.[].productId").type(JsonFieldType.NUMBER)
                            .optional()
                            .description("상품 아이디"),
                        fieldWithPath("data.[].eventPrizes.[].productName").type(JsonFieldType.STRING)
                            .optional()
                            .description("상품 이름"),
                        fieldWithPath("data.[].eventPrizes.[].productImg").type(JsonFieldType.STRING)
                            .optional()
                            .description("상품 이미지"),
                        fieldWithPath("data.[].eventPrizes.[].couponId").type(JsonFieldType.NUMBER)
                            .optional()
                            .description("쿠폰 아이디"),
                        fieldWithPath("data.[].eventPrizes.[].couponDetail").type(JsonFieldType.STRING)
                            .optional()
                            .description("쿠폰 상세"),
                        fieldWithPath("data.[].eventPrizes.[].type").type(JsonFieldType.STRING)
                            .optional()
                            .description("쿠폰 타입"),
                        fieldWithPath("data.[].eventPrizes.[].couponDiscountValue").type(
                                JsonFieldType.NUMBER).optional()
                            .description("쿠폰 할인량"),
                        fieldWithPath("data.[].eventPrizes.[].expiresAt").type(JsonFieldType.STRING)
                            .optional()
                            .description("쿠폰 유효기간"),
                        fieldWithPath("data.[].eventTimeStartAt").type(JsonFieldType.STRING)
                            .description("이벤트 시작시간")
                    )
                )
            ).when().port(port)
            .get("/api/v1/event/seller/participant" + "/{eventId}", eventId);

        assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

}

