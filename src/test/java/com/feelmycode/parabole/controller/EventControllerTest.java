package com.feelmycode.parabole.controller;

import static io.restassured.RestAssured.given;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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
import com.feelmycode.parabole.dto.EventCreateRequestDto;
import com.feelmycode.parabole.dto.EventPrizeCreateRequestDto;
import com.feelmycode.parabole.dto.UserDto;
import com.feelmycode.parabole.global.util.JwtUtils;
import com.feelmycode.parabole.repository.EventPrizeRepository;
import com.feelmycode.parabole.repository.EventRepository;
import com.feelmycode.parabole.repository.ProductRepository;
import com.feelmycode.parabole.repository.SellerRepository;
import com.feelmycode.parabole.repository.UserRepository;
import com.feelmycode.parabole.service.EventService;
import groovy.util.logging.Slf4j;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
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
@Slf4j
public class EventControllerTest {

    String outputDirectory = "./src/docs/asciidoc/snippets";

    @LocalServerPort
    int port;
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(outputDirectory);

    private RequestSpecification spec;

    @Autowired
    private JwtUtils jwtUtils;

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
    @Autowired
    EventPrizeRepository eventPrizeRepository;

    String BASIC_PATH = "/api/v1/event";

    public String toJsonString(Object data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        return mapper.writeValueAsString(data);
    }

    private String getToken(final UserDto request) {

        final ExtractableResponse<Response> response = RestAssured
            .given()
            .contentType(ContentType.JSON).body(request)
            .when()
            .post("/api/v1/auth/signin")
            .then()
            .extract();

        return response.body().jsonPath().get("data.token").toString();
    }

    @Before
    public void setUp() {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
            .build();
    }

    @Test
    @DisplayName("????????? ??????")
    public void createEvent() throws JSONException {

        // given
        UserDto request = UserDto.builder().email("thepara@bole.com").password("1234").build();
        String token = getToken(request);

        LocalDateTime startAt = LocalDateTime.parse("2022-11-10T00:00:00", ISO_LOCAL_DATE_TIME);
        LocalDateTime endAt = LocalDateTime.parse("2022-11-28T18:00:00", ISO_LOCAL_DATE_TIME);

        Seller seller = sellerRepository.findById(1L).orElseThrow();
        Product product = new Product(seller, 1, 50L, "CATEGORY", "thumb.img", "????????? ????????? ??????",
            50000L);
        productRepository.save(product);

        List<EventPrizeCreateRequestDto> prizes = new ArrayList<>();
        prizes.add(new EventPrizeCreateRequestDto(product.getId(), "PRODUCT", 5));

        EventCreateRequestDto requestDto = new EventCreateRequestDto(
            "SELLER", "RAFFLE", "??????????????? WEEK #????????????", startAt, endAt,
            "(????????? ?????????) ??????????????? ?????? ?????? ??????????????? ?????? ?????? ??? ????????????!", prizes
        );
        EventImage eventImage = new EventImage("banner.url", "detail.url");

        net.minidev.json.JSONObject jsonObject = new net.minidev.json.JSONObject();
        jsonObject.put("eventDtos", requestDto);
        jsonObject.put("image", eventImage);

        // when
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)
            .header("Authorization", "Bearer " + token)
            .filter(document("event-create",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("eventDtos").type(JsonFieldType.OBJECT).description("????????? ????????? ??????"),
                    fieldWithPath("eventDtos.userId").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                    fieldWithPath("eventDtos.createdBy").type(JsonFieldType.STRING).description("????????? ????????? (?????????:ADMIN / ?????????:SELLER)"),
                    fieldWithPath("eventDtos.type").type(JsonFieldType.STRING).description("????????? ?????? (RAFFLE:?????? / FCFS:?????????)"),
                    fieldWithPath("eventDtos.title").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("eventDtos.startAt").type(JsonFieldType.OBJECT).description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("eventDtos.endAt").type(JsonFieldType.OBJECT).description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("eventDtos.descript").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("eventDtos.eventImage").type(JsonFieldType.NULL).description("????????? ?????????(multipart??? ?????? ?????????)"),
                    fieldWithPath("image").type(JsonFieldType.OBJECT).description("????????? ?????????(URL)"),
                    fieldWithPath("image.eventBannerImg").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????????(URL)"),
                    fieldWithPath("image.eventDetailImg").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????????(URL)"),
                    fieldWithPath("eventDtos.eventPrizeCreateRequestDtos").type(JsonFieldType.ARRAY)
                        .description("????????? ?????? ??????"),
                    fieldWithPath("eventDtos.eventPrizeCreateRequestDtos.[].id").type(JsonFieldType.NUMBER)
                        .description("????????? ?????? ID"),
                    fieldWithPath("eventDtos.eventPrizeCreateRequestDtos.[].type").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (??????: PRODUCT /??????: COUPON )"),
                    fieldWithPath("eventDtos.eventPrizeCreateRequestDtos.[].stock").type(JsonFieldType.NUMBER)
                        .description("????????? ?????? ??????")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").description("????????? ??????")
                )))
            .body(jsonObject.toJSONString())
            .when().post(BASIC_PATH);

        Assertions.assertEquals(HttpStatus.CREATED.value(), resp.statusCode());

        /**
         * DELETE
         */
        JSONObject jObj = new JSONObject(resp.getBody().asString());
        Integer eventId = (Integer) jObj.get("data");
        eventRepository.delete(eventRepository.findById(Long.valueOf(eventId)).orElseThrow());
        productRepository.delete(product);

    }

    @Test
    @DisplayName("????????? ?????? ??????")
    public void getEventByEventId() {

        // given
        Long eventId = 1L;

        // when
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)

            .filter(document("event-id",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("eventId").description("????????? ?????????")),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ??????"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("????????? ??????"),
                    fieldWithPath("data.storeName").type(JsonFieldType.STRING)
                        .description("?????? ????????? ??????"),
                    fieldWithPath("data.sellerId").type(JsonFieldType.NUMBER).description("?????? ??????"),
                    fieldWithPath("data.createdBy").type(JsonFieldType.STRING)
                        .description("????????? ????????? (?????????:ADMIN / ?????????:SELLER)"),
                    fieldWithPath("data.type").type(JsonFieldType.STRING)
                        .description("????????? ?????? (RAFFLE:?????? / FCFS:?????????)"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.startAt").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.endAt").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.status").type(JsonFieldType.NUMBER)
                        .description("????????? ?????? ?????? (0: ????????? / 1: ????????? / 2: ??????)"),
                    fieldWithPath("data.descript").type(JsonFieldType.STRING)
                        .description("????????? ?????? ??????"),
                    fieldWithPath("data.eventImage").type(JsonFieldType.OBJECT)
                        .description("????????? ????????? ??????"),
                    fieldWithPath("data.eventImage.eventBannerImg").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????????(URL)"),
                    fieldWithPath("data.eventImage.eventDetailImg").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????????(URL)"),
                    fieldWithPath("data.eventPrizes").type(JsonFieldType.ARRAY)
                        .description("????????? ?????? ??????"),
                    fieldWithPath("data.eventPrizes.[].eventPrizeId").type(JsonFieldType.NUMBER)
                        .description("????????? ?????? ??????"),
                    fieldWithPath("data.eventPrizes.[].prizeType").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (??????: PRODUCT /??????: COUPON )"),
                    fieldWithPath("data.eventPrizes.[].stock").optional()
                        .description("?????? ??????"),
                    fieldWithPath("data.eventPrizes.[].productId").optional()
                        .description("?????? ?????????"),
                    fieldWithPath("data.eventPrizes.[].productName").optional()
                        .description("?????????"),
                    fieldWithPath("data.eventPrizes.[].productImg").optional()
                        .description("?????? ?????????(URL)"),
                    fieldWithPath("data.eventPrizes.[].couponId").optional()
                        .description("?????? ?????????"),
                    fieldWithPath("data.eventPrizes.[].couponDetail").optional()
                        .description("?????? ?????? ??????").optional(),
                    fieldWithPath("data.eventPrizes.[].type")
                        .description("?????? ?????? ?????? (?????????: RATE / ????????????: AMOUNT)").optional(),
                    fieldWithPath("data.eventPrizes.[].couponDiscountValue").optional()
                        .description("?????? ?????????"),
                    fieldWithPath("data.eventPrizes.[].expiresAt").optional()
                        .description("?????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)")
                )
            )).when().get(BASIC_PATH + "/{eventId}", eventId);

        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());

    }


    @Test
    @DisplayName("?????? ????????? ??????")
    public void getAllEvents() {
        // when
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)

            .filter(document("event-all",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ??????"),
                    fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("????????? ??????"),
                    fieldWithPath("data.[].storeName").type(JsonFieldType.STRING)
                        .description("?????? ????????? ??????"),
                    fieldWithPath("data.[].sellerId").type(JsonFieldType.NUMBER)
                        .description("?????? ??????"),
                    fieldWithPath("data.[].createdBy").type(JsonFieldType.STRING)
                        .description("????????? ????????? (?????????:ADMIN / ?????????:SELLER)"),
                    fieldWithPath("data.[].type").type(JsonFieldType.STRING)
                        .description("????????? ?????? (RAFFLE:?????? / FCFS:?????????)"),
                    fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.[].startAt").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.[].endAt").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.[].status").type(JsonFieldType.NUMBER)
                        .description("????????? ?????? ?????? (0: ????????? / 1: ????????? / 2: ??????)"),
                    fieldWithPath("data.[].descript").type(JsonFieldType.STRING)
                        .description("????????? ?????? ??????"),
                    fieldWithPath("data.[].eventImage").type(JsonFieldType.OBJECT)
                        .description("????????? ?????????(URL) ??????"),
                    fieldWithPath("data.[].eventImage.eventBannerImg").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????????(URL)"),
                    fieldWithPath("data.[].eventImage.eventDetailImg").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????????(URL)"),
                    fieldWithPath("data.[].eventPrizes").type(JsonFieldType.ARRAY)
                        .description("????????? ?????? ??????"),
                    fieldWithPath("data.[].eventPrizes[].eventPrizeId").type(JsonFieldType.NUMBER)
                        .description("????????? ?????? ??????"),
                    fieldWithPath("data.[].eventPrizes[].prizeType").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (??????: PRODUCT /??????: COUPON )"),
                    fieldWithPath("data.[].eventPrizes[].stock").optional()
                        .description("?????? ??????"),
                    fieldWithPath("data.[].eventPrizes.[].productId").optional()
                        .description("?????? ?????????"),
                    fieldWithPath("data.[].eventPrizes[].productName").optional()
                        .description("?????????"),
                    fieldWithPath("data.[].eventPrizes[].productImg").optional()
                        .description("?????? ?????????(URL)"),
                    fieldWithPath("data.[].eventPrizes[].couponId").optional()
                        .description("?????? ?????????"),
                    fieldWithPath("data.[].eventPrizes[].couponDetail").optional()
                        .description("?????? ?????? ??????"),
                    fieldWithPath("data.[].eventPrizes[].type").description(
                        "?????? ?????? ?????? (?????????: RATE / ????????????: AMOUNT)").optional(),
                    fieldWithPath("data.[].eventPrizes[].couponDiscountValue").optional()
                        .description(
                            "?????? ?????????"),
                    fieldWithPath("data.[].eventPrizes[].expiresAt").optional()
                        .description("?????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)")
                )
            )).when().get(BASIC_PATH);

        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("????????? ????????? ?????? ??????")
    public void getEventBySellerId() {

        // given
        UserDto request = UserDto.builder().email("thepara@bole.com").password("1234").build();
        String token = getToken(request);

        // when
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .port(port)
            .filter(document("event-sellerId",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ??????"),
                    fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("????????? ??????"),
                    fieldWithPath("data.[].storeName").type(JsonFieldType.STRING)
                        .description("?????? ????????? ??????"),
                    fieldWithPath("data.[].sellerId").type(JsonFieldType.NUMBER)
                        .description("?????? ??????"),
                    fieldWithPath("data.[].createdBy").type(JsonFieldType.STRING)
                        .description("????????? ????????? (?????????:ADMIN / ?????????:SELLER)"),
                    fieldWithPath("data.[].type").type(JsonFieldType.STRING)
                        .description("????????? ?????? (RAFFLE:?????? / FCFS:?????????)"),
                    fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.[].startAt").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.[].endAt").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.[].status").type(JsonFieldType.NUMBER)
                        .description("????????? ?????? ?????? (0: ????????? / 1: ????????? / 2: ??????)"),
                    fieldWithPath("data.[].descript").type(JsonFieldType.STRING)
                        .description("????????? ?????? ??????"),
                    fieldWithPath("data.[].eventImage").type(JsonFieldType.OBJECT)
                        .description("????????? ?????????(URL) ??????"),
                    fieldWithPath("data.[].eventImage.eventBannerImg").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????????(URL)"),
                    fieldWithPath("data.[].eventImage.eventDetailImg").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????????(URL)"),
                    fieldWithPath("data.[].eventPrizes").type(JsonFieldType.ARRAY)
                        .description("????????? ?????? ??????"),
                    fieldWithPath("data.[].eventPrizes[].eventPrizeId").type(JsonFieldType.NUMBER)
                        .description("????????? ?????? ??????"),
                    fieldWithPath("data.[].eventPrizes[].prizeType").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (??????: PRODUCT /??????: COUPON)"),
                    fieldWithPath("data.[].eventPrizes[].stock").optional()
                        .description("?????? ??????"),
                    fieldWithPath("data.[].eventPrizes.[].productId").optional()
                        .description("?????? ?????????"),
                    fieldWithPath("data.[].eventPrizes[].productName").optional()
                        .description("?????????"),
                    fieldWithPath("data.[].eventPrizes[].productImg").optional()
                        .description("?????? ?????????(URL)"),
                    fieldWithPath("data.[].eventPrizes[].couponId").optional()
                        .description("?????? ?????????"),
                    fieldWithPath("data.[].eventPrizes[].couponDetail").optional()
                        .description("?????? ?????? ??????"),
                    fieldWithPath("data.[].eventPrizes[].type").optional()
                        .description("?????? ?????? ?????? (?????????: RATE / ????????????: AMOUNT)"),
                    fieldWithPath("data.[].eventPrizes[].couponDiscountValue").optional()
                        .description(
                            "?????? ?????????"),
                    fieldWithPath("data.[].eventPrizes[].expiresAt").optional()
                        .description("?????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)")
                )
            )).when().get(BASIC_PATH + "/seller");

        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());

    }

    @Test
    @DisplayName("?????? ???????????? ????????? ?????? ??????")
    public void getEventBySearchOptions() {

        // given
        String eventType = "RAFFLE";
        String eventTitle = "?????????";
        Integer dateDiv = 0;
        String fromDateTime = "2022-11-02T15:00:00";
        String toDateTime = "2022-11-30T15:00:00";

        // when
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)
            .param("eventType", eventType)
            .param("eventTitle", "")
            .param("dateDiv", "")
            .param("fromDateTime", "")
            .param("toDateTime", "")
            .param("eventStatus", "")
            .filter(document("event-search",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("eventType").description("????????? ?????? (RAFFLE:?????? / FCFS:?????????)"),
                    parameterWithName("eventTitle").description("????????? ??????"),
                    parameterWithName("dateDiv").description(
                        "?????? ?????? ?????? (0: ????????? ???????????? / 1: ????????? ????????????)"),
                    parameterWithName("fromDateTime").description("?????? ?????? ??????"),
                    parameterWithName("toDateTime").description("?????? ?????? ??????"),
                    parameterWithName("eventStatus").description(
                        "????????? ?????? ?????? (0: ????????? / 1: ????????? / 2: ??????)")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ??????"),
                    fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("????????? ??????"),
                    fieldWithPath("data.[].sellerId").type(JsonFieldType.NUMBER)
                        .description("?????? ??????"),
                    fieldWithPath("data.[].createdBy").type(JsonFieldType.STRING)
                        .description("????????? ????????? (?????????:ADMIN / ?????????:SELLER)"),
                    fieldWithPath("data.[].type").type(JsonFieldType.STRING)
                        .description("????????? ?????? (RAFFLE:?????? / FCFS:?????????)"),
                    fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.[].startAt").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.[].endAt").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.[].status").type(JsonFieldType.NUMBER)
                        .description("????????? ?????? ?????? (0: ????????? / 1: ????????? / 2: ??????)"),
                    fieldWithPath("data.[].descript").type(JsonFieldType.STRING)
                        .description("????????? ?????? ??????"),
                    fieldWithPath("data.[].eventImage").type(JsonFieldType.OBJECT)
                        .description("????????? ?????????(URL) ??????"),
                    fieldWithPath("data.[].eventImage.eventBannerImg").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????????(URL)"),
                    fieldWithPath("data.[].eventImage.eventDetailImg").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????????(URL)")
                )
            )).log().all()
            .when().get(BASIC_PATH + "/list");

        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());

    }


    @Test
    @DisplayName("????????? ?????? ?????? ?????? ??????")
    public void showIsAvailable() {

        // given
        UserDto request = UserDto.builder().email("thepara@bole.com").password("1234").build();
        String token = getToken(request);
        Long userId = jwtUtils.extractUserId(token);

        String inputDtm = "2022-11-11T15:00:00";

        // when
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)
            .header("Authorization", "Bearer " + token)
            .param("inputDtm", inputDtm)
            .filter(document("event-create-check",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("inputDtm").description("?????? ?????? ?????? (yyyy-MM-ddTHH:mm:ss)")
                ),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").description("????????? ?????? ?????? ??????")
                )
            )).log().all().when().get(BASIC_PATH + "/seller/check");

        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());

    }


    @Test
    @DisplayName("????????? ????????? ??????")
    public void getEventsforScheduler() {

        // when
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)
            .filter(document("event-scheduler",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").type(JsonFieldType.ARRAY).description("?????? ??????"),
                    fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("????????? ??????"),
                    fieldWithPath("data.[].sellerId").type(JsonFieldType.NUMBER)
                        .description("?????? ??????"),
                    fieldWithPath("data.[].createdBy").type(JsonFieldType.STRING)
                        .description("????????? ????????? (?????????:ADMIN / ?????????:SELLER)"),
                    fieldWithPath("data.[].type").type(JsonFieldType.STRING)
                        .description("????????? ?????? (RAFFLE:?????? / FCFS:?????????)"),
                    fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("????????? ??????"),
                    fieldWithPath("data.[].startAt").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.[].endAt").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????? (yyyy-MM-dd'T'HH:mm:ss)"),
                    fieldWithPath("data.[].status").type(JsonFieldType.NUMBER)
                        .description("????????? ?????? ?????? (0: ????????? / 1: ????????? / 2: ??????)"),
                    fieldWithPath("data.[].descript").type(JsonFieldType.STRING)
                        .description("????????? ?????? ??????"),
                    fieldWithPath("data.[].eventImage").type(JsonFieldType.OBJECT)
                        .description("????????? ?????????(URL) ??????"),
                    fieldWithPath("data.[].eventImage.eventBannerImg").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????????(URL)"),
                    fieldWithPath("data.[].eventImage.eventDetailImg").type(JsonFieldType.STRING)
                        .description("????????? ?????? ?????????(URL)")
                )
            ))
            .when().get(BASIC_PATH + "/seller/scheduler");

        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());

    }

    @Test
    @DisplayName("????????? ??????")
    public void deleteEvent() {

        // given
        UserDto request = UserDto.builder().email("thepara@bole.com").password("1234").build();
        String token = getToken(request);
        Long userId = jwtUtils.extractUserId(token);

        LocalDateTime startAt = LocalDateTime.parse("2022-12-10T00:00:00", ISO_LOCAL_DATE_TIME);
        LocalDateTime endAt = LocalDateTime.parse("2022-12-28T18:00:00", ISO_LOCAL_DATE_TIME);

        Seller seller = sellerRepository.findById(1L).orElseThrow();
        Product product = new Product(seller, 1, 50L, "CATEGORY", "thumb.img", "????????? ????????? ??????",
            50000L);
        productRepository.save(product);

        List<EventPrizeCreateRequestDto> prizes = new ArrayList<>();
        prizes.add(new EventPrizeCreateRequestDto(product.getId(), "PRODUCT", 40));
        EventCreateRequestDto requestDto = new EventCreateRequestDto(
            "SELLER", "RAFFLE", "????????? ?????? BY REST DOCS", startAt, endAt,
            "????????? ?????? v2", prizes
        );

        Long eventId = eventService.createEvent(userId, requestDto);

        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .port(port)
            .header("Authorization", "Bearer " + token)
            .filter(document("event-delete",
                preprocessRequest(modifyUris()
                        .scheme("https")
                        .host("parabole.com"),
                    prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("eventId").description("????????? ?????????")),
                responseFields(
                    fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                    fieldWithPath("data").description("null")
                )))
            .when().delete(BASIC_PATH + "/{eventId}", eventId);

        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());

        /**
         * DELETE
         */
        eventRepository.delete(eventRepository.findById(eventId).orElseThrow());
        productRepository.delete(product);


    }
}
