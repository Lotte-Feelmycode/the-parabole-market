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
import com.feelmycode.parabole.dto.UserDto;
import io.restassured.response.ExtractableResponse;
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
    @DisplayName("??????: ????????? ?????? ?????? ??????")
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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ??????"),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("?????? ??????"),
                        fieldWithPath("data.content.[].name").type(JsonFieldType.STRING)
                            .description("?????????").optional(),
                        fieldWithPath("data.content.[].serialNo").type(JsonFieldType.STRING)
                            .description("?????? ????????????").optional(),
                        fieldWithPath("data.content.[].sellerName").type(JsonFieldType.STRING)
                            .description("?????? ??????").optional(),
                        fieldWithPath("data.content.[].type").type(JsonFieldType.STRING)
                            .description("?????? ??????").optional(),
                        fieldWithPath("data.content.[].discountValue").type(JsonFieldType.NUMBER)
                            .description("?????? ???(??????/??????)").optional(),
                        fieldWithPath("data.content.[].useState").type(JsonFieldType.STRING)
                            .description("?????? ??????").optional(),
                        fieldWithPath("data.content.[].useDate").type(JsonFieldType.STRING)
                            .description("????????????").optional(),
                        fieldWithPath("data.content.[].acquiredDate").type(JsonFieldType.STRING)
                            .description("????????????").optional(),
                        fieldWithPath("data.content.[].validAt").type(JsonFieldType.STRING)
                            .description("??????????????????").optional(),
                        fieldWithPath("data.content.[].expiresAt").type(JsonFieldType.STRING)
                            .description("????????????").optional(),
                        fieldWithPath("data.pageable").type(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                            .description("????????? ????????? ??????"),
                        fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                            .description("?????? ????????? ??????"),
                        fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                            .description("??? ?????? ??????"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                            .description("????????? ??? ?????? ??????"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER)
                            .description("?????? ?????????"),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT)
                            .description("??????"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                            .description("?????? ?????? ??????"),
                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
                            .description("?????? ?????? ??????"),
                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
                            .description("????????? ?????? ??????"),
                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
                            .description("??? ????????? ??????"),
                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                            .description("??? ?????? ??????"),
                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN)
                            .description("??? ??????"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                            .description("????????? ????????? ??????"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("???????????? ?????? ???"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("?????? ??????"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                            .description("?????? ?????? ??????")
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
    @DisplayName("??????: ????????? ?????? ?????? ??????")
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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ??????"),
                        fieldWithPath("data.content").type(JsonFieldType.ARRAY).description("?????? ??????"),
                        fieldWithPath("data.content.[].couponId").type(JsonFieldType.NUMBER)
                            .description("?????? ID").optional(),
                        fieldWithPath("data.content.[].name").type(JsonFieldType.STRING)
                            .description("?????? ??????").optional(),
                        fieldWithPath("data.content.[].type").type(JsonFieldType.NUMBER)
                            .description("?????? ??????").optional(),
                        fieldWithPath("data.content.[].discountValue").type(JsonFieldType.NUMBER)
                            .description("?????? ???(??????/??????)").optional(),
                        fieldWithPath("data.content.[].createdAt").type(JsonFieldType.STRING)
                            .description("????????????").optional(),
                        fieldWithPath("data.content.[].validAt").type(JsonFieldType.STRING)
                            .description("??????????????????").optional(),
                        fieldWithPath("data.content.[].expiresAt").type(JsonFieldType.STRING)
                            .description("????????????").optional(),
                        fieldWithPath("data.content.[].detail").type(JsonFieldType.STRING)
                            .description("?????? ?????? ??????").optional(),
                        fieldWithPath("data.content.[].cnt").type(JsonFieldType.NUMBER)
                            .description("?????? ?????? ??????").optional(),
                        fieldWithPath("data.content.[].remains").type(JsonFieldType.NUMBER)
                            .description("?????? ?????? ??????").optional(),
                        fieldWithPath("data.pageable").type(JsonFieldType.STRING).description("????????? ??????"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                            .description("????????? ????????? ??????"),
                        fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                            .description("?????? ????????? ??????"),
                        fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                            .description("??? ?????? ??????"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                            .description("????????? ??? ?????? ??????"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER)
                            .description("?????? ?????????"),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT)
                            .description("??????"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                            .description("?????? ?????? ??????"),
                        fieldWithPath("data.sort.sorted").type(JsonFieldType.BOOLEAN)
                            .description("?????? ?????? ??????"),
                        fieldWithPath("data.sort.unsorted").type(JsonFieldType.BOOLEAN)
                            .description("????????? ?????? ??????"),
                        fieldWithPath("data.first").type(JsonFieldType.BOOLEAN)
                            .description("??? ????????? ??????"),
                        fieldWithPath("data.numberOfElements").type(JsonFieldType.NUMBER)
                            .description("??? ?????? ??????"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN)
                            .description("????????? ????????? ??????"),
                        fieldWithPath("data.size").type(JsonFieldType.NUMBER).description("???????????? ?????? ???"),
                        fieldWithPath("data.number").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                        fieldWithPath("data.sort").type(JsonFieldType.OBJECT).description("?????? ??????"),
                        fieldWithPath("data.sort.empty").type(JsonFieldType.BOOLEAN)
                            .description("?????? ?????? ??????"),
                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("??? ??????")
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
    @DisplayName("?????? ????????? ?????? ??????")
    public void addCoupon() {

        String token = jwtUtils.generateToken(userRepository.findById(1L).orElseThrow());

        LocalDateTime validAt = LocalDateTime.parse("2022-09-16T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime expiresAt = LocalDateTime.parse("2022-11-30T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        CouponCreateRequestDto dto =
            new CouponCreateRequestDto("??????????????? ?????? ??????", 2, 2000, validAt, expiresAt, "????????? ??????????????? ?????????", 5);

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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("????????? ?????? ??????"),
                        fieldWithPath("data.couponId").type(JsonFieldType.NUMBER)
                            .description("?????? ID"),
                        fieldWithPath("data.couponName").type(JsonFieldType.STRING)
                            .description("?????? ??????"),
                        fieldWithPath("data.sellerStorename").type(JsonFieldType.STRING)
                            .description("????????? ????????? ??????"),
                        fieldWithPath("data.type").type(JsonFieldType.STRING)
                            .description("?????? ??????"),
                        fieldWithPath("data.cnt").type(JsonFieldType.NUMBER)
                            .description("?????? ?????? ??????")
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
    @DisplayName("?????? ???????????? ????????? ??????")
    public void assignCoupon() {

        Seller seller = new Seller();
        String token = jwtUtils.generateToken(new User().builder().seller(seller).build());

        LocalDateTime validAt = LocalDateTime.parse("2022-09-16T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime expiresAt = LocalDateTime.parse("2022-11-30T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        CouponCreateRequestDto dto = new CouponCreateRequestDto("??????????????? ?????? ??????", 2, 2000,
            validAt, expiresAt, "????????? ??????????????? ?????????", 5);

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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ??????").optional()
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
