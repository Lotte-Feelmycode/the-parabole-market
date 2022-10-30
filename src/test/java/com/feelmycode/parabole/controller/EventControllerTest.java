package com.feelmycode.parabole.controller;

import static io.restassured.RestAssured.given;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feelmycode.parabole.domain.EventImage;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventPrizeCreateRequestDto;
import com.feelmycode.parabole.enumtype.CouponType;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.ProductRepository;
import com.feelmycode.parabole.repository.SellerRepository;
import com.feelmycode.parabole.repository.UserRepository;
import com.feelmycode.parabole.service.EventService;
import groovy.util.logging.Slf4j;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@Slf4j
public class EventControllerTest {

    @LocalServerPort
    int port;
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    private RequestSpecification spec;

    @Autowired
    private EventService eventService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    String BASIC_PATH = "/api/v1/event";

    public String toJsonString(Object data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        return mapper.writeValueAsString(data);
    }

    @Before
    public void setUp() {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
            .build();
    }


    @Test
    @DisplayName("이벤트 등록")
    @Transactional
    public void createEvent() {

        // given
        /*
        User user = userRepository.save(
            new User("eventTest@mail.com", "eventTest", "eventNickname", "010-3354-3342", "1234"));
        Seller seller = new Seller("event Store Name", "3245918723");
        user.setSeller(seller);
        sellerRepository.save(seller);
        */
        LocalDateTime startAt = LocalDateTime.parse("2022-11-10T00:00:00", ISO_LOCAL_DATE_TIME);
        LocalDateTime endAt = LocalDateTime.parse("2022-11-28T18:00:00", ISO_LOCAL_DATE_TIME);

        Seller seller = sellerRepository.findById(1L).orElseThrow();
        Product product = new Product(seller, 1, 50L, "CATEGORY", "thumb.img", "이벤트 테스트 상품",
            50000L);
        productRepository.save(product);

        List<EventPrizeCreateRequestDto> prizes = new ArrayList<>();
        prizes.add(new EventPrizeCreateRequestDto(product.getId(), "PRODUCT", 40));
        EventCreateRequestDto requestDto = new EventCreateRequestDto(
            seller.getUser().getId(), "SELLER", "RAFFLE", "이벤트 제목 BY REST DOCS", startAt, endAt,
            "이벤트 설명 v2",
            new EventImage("banner.url", "detail.url"), prizes
        );

        String requestJson = null;
        try {
            requestJson = toJsonString(requestDto);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        // when
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)
            .filter(document("createEvent",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").description("이벤트 번호")
                )))
            .body(requestJson).log().all()
            .when().post(BASIC_PATH);

//        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());

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


    @Test
    @DisplayName("이벤트 취소")
//    @Transactional
    public void deleteEvent() {

        // given
        LocalDateTime startAt = LocalDateTime.parse("2022-11-10T00:00:00", ISO_LOCAL_DATE_TIME);
        LocalDateTime endAt = LocalDateTime.parse("2022-11-28T18:00:00", ISO_LOCAL_DATE_TIME);

        Seller seller = sellerRepository.findById(1L).orElseThrow();
        Product product = new Product(seller, 1, 50L, "CATEGORY", "thumb.img", "이벤트 테스트 상품",
            50000L);
        productRepository.save(product);

        List<EventPrizeCreateRequestDto> prizes = new ArrayList<>();
        prizes.add(new EventPrizeCreateRequestDto(product.getId(), "PRODUCT", 40));
        EventCreateRequestDto requestDto = new EventCreateRequestDto(
            seller.getUser().getId(), "SELLER", "RAFFLE", "이벤트 제목 BY REST DOCS", startAt, endAt,
            "이벤트 설명 v2",
            new EventImage("banner.url", "detail.url"), prizes
        );
        Long eventId = eventService.createEvent(requestDto);

        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)
            .filter(document("deleteEvent",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                    fieldWithPath("data").description("이벤트 번호")
                )))
            .log().all()
            .when().delete(BASIC_PATH+"/{eventId}", eventId);
    }
}
