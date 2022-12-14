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
    @DisplayName("????????? ??????")
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
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("eventId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("eventPrizeId").type(JsonFieldType.NUMBER)
                            .description("????????? ??????")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("?????? ??????")
                    )
                )
            ).when().port(port)
            .post("/api/v1/event/participant");

        //then
        assertEquals(HttpStatus.CREATED.value(), resp.statusCode());
        eventParticipantRepository.deleteByUserIdAndEventId(userId, eventId);
    }

    @Test
    @DisplayName("????????? ?????? ??????")
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
                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                        fieldWithPath("eventId").type(JsonFieldType.NUMBER).description("????????? ?????????")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("?????? ??????")
                    )
                )
            ).when().port(port)
            .post("/api/v1/event/participant/check");

        //then

        assertEquals(HttpStatus.ALREADY_REPORTED.value(), resp.statusCode());
    }

    @Test
    @DisplayName("?????? ????????? ?????? ????????? ??????")
    public void test02_getUserEventParticipants() {

        Long userId = 3L;

        Response resp = given(this.spec)

            .filter(document(
                    "user-eventparticipant",
                    preprocessRequest(modifyUris().scheme("http").host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ??????"),
                        fieldWithPath("data.[].userId").type(JsonFieldType.NUMBER).description("?????? ??????"),
                        fieldWithPath("data.[].eventId").type(JsonFieldType.NUMBER)
                            .description("????????? ??????"),
                        fieldWithPath("data.[].eventTimeStartAt").type(JsonFieldType.STRING)
                            .description("????????? ????????????(yyyy-MM-dd'T'HH:mm:ss.SSSSSS)"),
                        fieldWithPath("data.[].eventTitle").type(JsonFieldType.STRING)
                            .description("????????? ??????"),
                        fieldWithPath("data.[].startAt").type(JsonFieldType.STRING)
                            .description("????????? ????????????(yyyy-MM-dd'T'HH:mm:ss)"),
                        fieldWithPath("data.[].endAt").type(JsonFieldType.STRING)
                            .description("????????? ????????????(yyyy-MM-dd'T'HH:mm:ss)"),
                        fieldWithPath("data.[].status").type(JsonFieldType.NUMBER)
                            .description("????????? ??????"),
                        fieldWithPath("data.[].eventImg").type(JsonFieldType.STRING)
                            .description("????????? ?????????")
                    )
                )
            ).when().port(port)
            .get("/api/v1/event/user/participant" + "/{userId}", userId);

        assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("????????? ????????? ?????? ????????? ??????")
    public void test_getEventParticipants() {

        Long eventId = 2L;

        Response resp = given(this.spec)

            .filter(document(
                    "seller-eventparticipant",
                    preprocessRequest(modifyUris().scheme("http").host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ??????"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("?????? ??????"),
                        fieldWithPath("data.[].user").type(JsonFieldType.OBJECT)
                            .description("?????? ??????"),
                        fieldWithPath("data.[].user.id").type(JsonFieldType.NUMBER)
                            .description("?????? ?????????"),
                        fieldWithPath("data.[].user.email").type(JsonFieldType.STRING)
                            .description("?????? ?????????"),
                        fieldWithPath("data.[].user.name").type(JsonFieldType.STRING)
                            .description("?????? ??????"),
                        fieldWithPath("data.[].user.nickname").type(JsonFieldType.STRING)
                            .description("?????? ??????"),
                        fieldWithPath("data.[].user.phone").type(JsonFieldType.STRING)
                            .description("?????? ????????? ??????"),
                        fieldWithPath("data.[].eventPrizes.[]").type(JsonFieldType.ARRAY)
                            .description("?????? ?????? ?????? ??????"),
                        fieldWithPath("data.[].eventPrizes.[].eventPrizeId").type(JsonFieldType.NUMBER)
                            .optional()
                            .description("????????? ?????? ?????????"),
                        fieldWithPath("data.[].eventPrizes.[].prizeType").type(JsonFieldType.STRING)
                            .optional()
                            .description("????????? ?????? ??????"),
                        fieldWithPath("data.[].eventPrizes.[].stock").type(JsonFieldType.NUMBER)
                            .optional()
                            .description("????????? ?????? ??????"),
                        fieldWithPath("data.[].eventPrizes.[].productId").type(JsonFieldType.NUMBER)
                            .optional()
                            .description("?????? ?????????"),
                        fieldWithPath("data.[].eventPrizes.[].productName").type(JsonFieldType.STRING)
                            .optional()
                            .description("?????? ??????"),
                        fieldWithPath("data.[].eventPrizes.[].productImg").type(JsonFieldType.STRING)
                            .optional()
                            .description("?????? ?????????"),
                        fieldWithPath("data.[].eventPrizes.[].couponId").type(JsonFieldType.NUMBER)
                            .optional()
                            .description("?????? ?????????"),
                        fieldWithPath("data.[].eventPrizes.[].couponDetail").type(JsonFieldType.STRING)
                            .optional()
                            .description("?????? ??????"),
                        fieldWithPath("data.[].eventPrizes.[].type").type(JsonFieldType.STRING)
                            .optional()
                            .description("?????? ??????"),
                        fieldWithPath("data.[].eventPrizes.[].couponDiscountValue").type(
                                JsonFieldType.NUMBER).optional()
                            .description("?????? ?????????"),
                        fieldWithPath("data.[].eventPrizes.[].expiresAt").type(JsonFieldType.STRING)
                            .optional()
                            .description("?????? ????????????"),
                        fieldWithPath("data.[].eventTimeStartAt").type(JsonFieldType.STRING)
                            .description("????????? ????????????(yyyy-MM-dd`T`HH:mm:ss)")
                    )
                )
            ).when().port(port)
            .get("/api/v1/event/seller/participant" + "/{eventId}", eventId);

        assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

}

