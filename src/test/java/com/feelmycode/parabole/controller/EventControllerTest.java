package com.feelmycode.parabole.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
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

import com.feelmycode.parabole.enumtype.CouponType;
import com.feelmycode.parabole.service.EventService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class EventControllerTest {

    @LocalServerPort
    int port;
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private RequestSpecification spec;

    @Autowired
    private EventService eventService;

    String BASIC_PATH = "/api/v1/event";

    @Before
    public void setUp() {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
            .build();
    }


    @Test
    @DisplayName("이벤트 단건 조회")
    public void getEventByEventId() {

        // given
        Long eventId = 1L;

        // when
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)

            .filter(document("eventById",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("이벤트 번호"),
                    fieldWithPath("data.storeName").type(JsonFieldType.STRING)
                        .description("셀러 스토어 이름"),
                    fieldWithPath("data.sellerId").type(JsonFieldType.NUMBER).description("셀러 번호"),
                    fieldWithPath("data.createdBy").type(JsonFieldType.STRING)
                        .description("이벤트 생성자 유형"),
                    fieldWithPath("data.type").type(JsonFieldType.STRING).description("이벤트 유형"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("이벤트 제목"),
                    fieldWithPath("data.startAt").type(JsonFieldType.STRING)
                        .description("이벤트 시작 일시"),
                    fieldWithPath("data.endAt").type(JsonFieldType.STRING).description("이벤트 종료 일시"),
                    fieldWithPath("data.status").type(JsonFieldType.NUMBER)
                        .description("이벤트 진행 상태"),
                    fieldWithPath("data.descript").type(JsonFieldType.STRING)
                        .description("이벤트 상세 설명"),
                    fieldWithPath("data.eventImage").type(JsonFieldType.OBJECT)
                        .description("이벤트 이미지 정보"),
                    fieldWithPath("data.eventImage.eventBannerImg").type(JsonFieldType.STRING)
                        .description("이벤트 배너 이미지"),
                    fieldWithPath("data.eventImage.eventDetailImg").type(JsonFieldType.STRING)
                        .description("이벤트 상세 이미지"),
                    fieldWithPath("data.eventPrizes").type(JsonFieldType.ARRAY)
                        .description("이벤트 경품 정보"),
                    fieldWithPath("data.eventPrizes.[].eventPrizeId").type(JsonFieldType.NUMBER)
                        .description("이벤트 경품 번호"),
                    fieldWithPath("data.eventPrizes.[].prizeType").type(JsonFieldType.STRING)
                        .description("이벤트 경품 유형"),
                    fieldWithPath("data.eventPrizes.[].stock")
                        .description("상품 재고"),
                    fieldWithPath("data.eventPrizes.[].productId")
                        .description("상품 아이디"),
                    fieldWithPath("data.eventPrizes.[].productName")
                        .description("상품명"),
                    fieldWithPath("data.eventPrizes.[].productImg")
                        .description("상품 이미지"),
                    fieldWithPath("data.eventPrizes.[].couponId")
                        .description("쿠폰 아이디"),
                    fieldWithPath("data.eventPrizes.[].couponDetail")
                        .description("쿠폰 상세 설명"),
                    fieldWithPath("data.eventPrizes.[].type")
                        .description("쿠폰 할인 유형"),
                    fieldWithPath("data.eventPrizes.[].couponDiscountValue").description("쿠폰 할인값"),
                    fieldWithPath("data.eventPrizes.[].expiresAt")
                        .description("쿠폰 만료 일시")
                )
            )).when().get(BASIC_PATH + "/{eventId}", eventId);

        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());

    }



    @Test
    @DisplayName("이벤트 생성")
    @Transactional
    @Rollback(true)
    public void createEvent() {

        // given

    }

//    @Test
//    @DisplayName("셀러의 이벤트 목록 조회")
//    public void getEventBySellerId() {
//
//        // given
//        Long userId = 1L;
//
//        // when
//        Response resp = given(this.spec)
//            .accept(ContentType.JSON)
//            .contentType(ContentType.JSON)
//            .port(port)
//
//            .filter(document("eventBySellerId",
//                preprocessRequest(modifyUris()
//                        .scheme("https")
//                        .host("parabole.com"),
//                    prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                responseFields(
//                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
//                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
//                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답 정보"),
//                    fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("이벤트 번호"),
//                    fieldWithPath("data.[].storeName").type(JsonFieldType.STRING)
//                        .description("셀러 스토어 이름"),
//                    fieldWithPath("data.[].sellerId").type(JsonFieldType.NUMBER).description("셀러 번호"),
//                    fieldWithPath("data.[].createdBy").type(JsonFieldType.STRING)
//                        .description("이벤트 생성자 유형"),
//                    fieldWithPath("data.[].type").type(JsonFieldType.STRING).description("이벤트 유형"),
//                    fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("이벤트 제목"),
//                    fieldWithPath("data.[].startAt").type(JsonFieldType.STRING)
//                        .description("이벤트 시작 일시"),
//                    fieldWithPath("data.[].endAt").type(JsonFieldType.STRING).description("이벤트 종료 일시"),
//                    fieldWithPath("data.[].status").type(JsonFieldType.NUMBER)
//                        .description("이벤트 진행 상태"),
//                    fieldWithPath("data.[].descript").type(JsonFieldType.STRING)
//                        .description("이벤트 상세 설명"),
//                    fieldWithPath("data.[].eventImage").type(JsonFieldType.OBJECT)
//                        .description("이벤트 이미지 정보"),
//                    fieldWithPath("data.[].eventImage.eventBannerImg").type(JsonFieldType.STRING)
//                        .description("이벤트 배너 이미지"),
//                    fieldWithPath("data.[].eventImage.eventDetailImg").type(JsonFieldType.STRING)
//                        .description("이벤트 상세 이미지"),
////                    fieldWithPath("data.[].eventPrizes").type(JsonFieldType.ARRAY)
////                        .description("이벤트 경품 정보")
//                    fieldWithPath("data.[].eventPrizes[].eventPrizeId").type(JsonFieldType.NUMBER)
//                        .description("이벤트 경품 번호"),
//                    fieldWithPath("data.[].eventPrizes[].prizeType").type(JsonFieldType.STRING)
//                        .description("이벤트 경품 유형"),
//                    fieldWithPath("data.[].eventPrizes[].stock")
//                        .description("상품 재고"),
//                    fieldWithPath("data.[].eventPrizes.[].productId")
//                        .description("상품 아이디"),
//                    fieldWithPath("data.[].eventPrizes[].productName")
//                        .description("상품명"),
//                    fieldWithPath("data.[].eventPrizes[].productImg")
//                        .description("상품 이미지"),
//                    fieldWithPath("data.[].eventPrizes[].couponId")
//                        .description("쿠폰 아이디"),
//                    fieldWithPath("data.[].eventPrizes[].couponDetail")
//                        .description("쿠폰 상세 설명"),
//                    fieldWithPath("data.[].eventPrizes[].type").description("쿠폰 할인 유형"),
//                    fieldWithPath("data.[].eventPrizes[].couponDiscountValue").description("쿠폰 할인값"),
//                    fieldWithPath("data.[].eventPrizes[].expiresAt")
//                        .description("쿠폰 만료 일시").ignored()
//                )
//            )).when().get(BASIC_PATH + "/seller/{userId}", userId);
//
//        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
////        System.out.println(resp.getBody().prettyPrint());
//
//    }
}
