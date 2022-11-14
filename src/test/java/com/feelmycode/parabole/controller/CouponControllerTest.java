package com.feelmycode.parabole.controller;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.feelmycode.parabole.domain.Seller;
import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.CouponAssignRequestDto;
import com.feelmycode.parabole.dto.CouponCreateRequestDto;
import com.feelmycode.parabole.dto.CouponCreateResponseDto;
import com.feelmycode.parabole.global.util.JwtUtils;
import com.feelmycode.parabole.global.util.StringUtil;
import com.feelmycode.parabole.repository.CouponRepository;
import com.feelmycode.parabole.repository.SellerRepository;
import com.feelmycode.parabole.repository.UserRepository;
import com.feelmycode.parabole.service.CouponService;
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
    private CouponService couponService;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Before
    public void setUp() {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
            .build();
    }


    @Test
    @DisplayName("유저: 보유한 쿠폰 목록 조회")
    public void couponListUser() {

        User user = userRepository.findById(10L).orElseThrow();
        String token = jwtUtils.generateToken(user);

        // When
        Response resp = given(this.spec)
            .header("authorization", "Bearer " + token)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("coupon-list-user",
                    preprocessRequest(modifyUris()
                            .scheme("https")
                            .host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보"),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("쿠폰 목록"),
                        fieldWithPath("data.content.[].name").type(JsonFieldType.STRING)
                            .description("쿠폰명").optional(),
                        fieldWithPath("data.content.[].serialNo").type(JsonFieldType.STRING)
                            .description("쿠폰 일련번호").optional(),
                        fieldWithPath("data.content.[].sellerName").type(JsonFieldType.STRING)
                            .description("셀러 이름").optional(),
                        fieldWithPath("data.content.[].type").type(JsonFieldType.STRING)
                            .description("쿠폰 유형").optional(),
                        fieldWithPath("data.content.[].discountValue").type(JsonFieldType.NUMBER)
                            .description("할인 값(비율/금액)").optional(),
                        fieldWithPath("data.content.[].useState").type(JsonFieldType.STRING)
                            .description("사용 여부").optional(),
                        fieldWithPath("data.content.[].useDate").type(JsonFieldType.STRING)
                            .description("사용일자").optional(),
                        fieldWithPath("data.content.[].acquiredDate").type(JsonFieldType.STRING)
                            .description("획득일자").optional(),
                        fieldWithPath("data.content.[].validAt").type(JsonFieldType.STRING)
                            .description("유효시작일자").optional(),
                        fieldWithPath("data.content.[].expiresAt").type(JsonFieldType.STRING)
                            .description("만료일자").optional(),
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
                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN)
                            .description("빈 여부"),
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
            .get("/api/v1/coupon/user/list");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }


    @Test
    @DisplayName("셀러: 발행한 쿠폰 목록 조회")
    public void couponListSeller() {

        String token = jwtUtils.generateToken(userRepository.findById(1L).orElseThrow());

        // When
        Response resp = given(this.spec)
            .header("authorization", "Bearer " + token)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("coupon-list-seller",
                    preprocessRequest(modifyUris()
                            .scheme("https")
                            .host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보"),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("쿠폰 목록"),
                        fieldWithPath("data.content.[].couponId").type(JsonFieldType.NUMBER)
                            .description("쿠폰 ID").optional(),
                        fieldWithPath("data.content.[].name").type(JsonFieldType.STRING)
                            .description("쿠폰 이름").optional(),
                        fieldWithPath("data.content.[].type").type(JsonFieldType.NUMBER)
                            .description("쿠폰 유형").optional(),
                        fieldWithPath("data.content.[].discountValue").type(JsonFieldType.NUMBER)
                            .description("할인 값(비율/금액)").optional(),
                        fieldWithPath("data.content.[].createdAt").type(JsonFieldType.STRING)
                            .description("생성일자").optional(),
                        fieldWithPath("data.content.[].validAt").type(JsonFieldType.STRING)
                            .description("유효시작일자").optional(),
                        fieldWithPath("data.content.[].expiresAt").type(JsonFieldType.STRING)
                            .description("만료일자").optional(),
                        fieldWithPath("data.content.[].detail").type(JsonFieldType.STRING)
                            .description("쿠폰 상세 설명").optional(),
                        fieldWithPath("data.content.[].cnt").type(JsonFieldType.NUMBER)
                            .description("쿠폰 발행 수량").optional(),
                        fieldWithPath("data.content.[].remains").type(JsonFieldType.NUMBER)
                            .description("쿠폰 잔여 수량").optional(),
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
            .get("/api/v1/coupon/seller/list");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());

    }

    @Test
    @DisplayName("셀러 새로운 쿠폰 등록")
    public void addCoupon() {

        String token = jwtUtils.generateToken(userRepository.findById(1L).orElseThrow());

        LocalDateTime validAt = LocalDateTime.parse("2022-09-16T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime expiresAt = LocalDateTime.parse("2022-11-30T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        CouponCreateRequestDto dto =
            new CouponCreateRequestDto("크리스마스 기념 쿠폰", 2, 2000, validAt, expiresAt, "행복한 크리스마스 되세요", 5);

        // When
        Response resp = given(this.spec)
            .accept(ContentType.JSON)
            .header("authorization", "Bearer " + token)
            .body(dto)
            .contentType(ContentType.JSON)
            .filter(document("coupon-add-seller",
                    preprocessRequest(modifyUris()
                            .scheme("https")
                            .host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("등록한 쿠폰 정보"),
                        fieldWithPath("data.couponId").type(JsonFieldType.NUMBER)
                            .description("쿠폰 ID"),
                        fieldWithPath("data.couponName").type(JsonFieldType.STRING)
                            .description("쿠폰 이름"),
                        fieldWithPath("data.sellerStorename").type(JsonFieldType.STRING)
                            .description("판매자 스토어 이름"),
                        fieldWithPath("data.type").type(JsonFieldType.STRING)
                            .description("쿠폰 유형"),
                        fieldWithPath("data.cnt").type(JsonFieldType.NUMBER)
                            .description("쿠폰 발행 수량")
                    )
                )
            )
            .when()
            .port(port)
            .post("/api/v1/coupon/new");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

    @Test
    @DisplayName("셀러 유저에게 쿠폰을 배정")
    public void assignCoupon() {

        Seller seller = new Seller();
        String token = jwtUtils.generateToken(new User().builder().seller(seller).build());

        LocalDateTime validAt = LocalDateTime.parse("2022-09-16T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime expiresAt = LocalDateTime.parse("2022-11-30T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        CouponCreateRequestDto dto = new CouponCreateRequestDto("크리스마스 기념 쿠폰", 2, 2000,
            validAt, expiresAt, "행복한 크리스마스 되세요", 5);

        CouponCreateResponseDto responseDto = couponService.addCoupon(seller.getId(), dto);

        User user100 = new User().builder().id(100L).build();
        User user101 = new User().builder().id(101L).build();
        User user102 = new User().builder().id(102L).build();

        CouponAssignRequestDto assignRequestDto = new CouponAssignRequestDto(responseDto.getCouponId(), Arrays.asList(100L, 101L, 102L));

        Response resp = given(this.spec)
            .header("authorization", "Bearer " + token)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(assignRequestDto)
            .filter(document("coupon-assign",
                    preprocessRequest(modifyUris()
                            .scheme("https")
                            .host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보").optional()
                    )
                )
            )
            .when()
            .port(port)
            .post("/api/v1/coupon/assign");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());

        // Rollback
        couponRepository.deleteById(responseDto.getCouponId());
        userRepository.delete(user100);
        userRepository.delete(user101);
        userRepository.delete(user102);

    }

}
