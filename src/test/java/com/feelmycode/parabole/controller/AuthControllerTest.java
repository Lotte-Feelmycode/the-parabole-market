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

import com.feelmycode.parabole.domain.User;
import com.feelmycode.parabole.dto.UserDto;
import com.feelmycode.parabole.global.util.StringUtil;
import com.feelmycode.parabole.repository.CartRepository;
import com.feelmycode.parabole.repository.UserRepository;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class AuthControllerTest {

    @LocalServerPort
    int port;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(StringUtil.ASCII_DOC_OUTPUT_DIR);
    private RequestSpecification spec;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;

    @Before
    public void setUp() {
        RestAssured.port = port;
        this.spec = new RequestSpecBuilder().addFilter(
                documentationConfiguration(this.restDocumentation))
            .build();
    }

    @Test
    @DisplayName("????????????")
    public void signup() {

        UserDto dto = UserDto.builder().email("user0@naver.com")
            .name("user").nickname("nickname").phone("01022222222").password("password")
            .passwordConfirmation("password").build();

        // When
        Response resp = given(this.spec)
            .body(dto)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("auth-signup",
                    preprocessRequest(modifyUris()
                            .scheme("https")
                            .host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ??????"),
                        fieldWithPath("data.token").type(JsonFieldType.STRING).description("????????? ??????")
                            .optional(),
                        fieldWithPath("data.email").type(JsonFieldType.STRING)
                            .description("?????????"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                            .description("????????????"),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                            .description("?????????").optional(),
                        fieldWithPath("data.phone").type(JsonFieldType.STRING)
                            .description("????????????").optional(),
                        fieldWithPath("data.password").type(JsonFieldType.STRING)
                            .description("????????????").optional(),
                        fieldWithPath("data.passwordConfirmation").type(JsonFieldType.STRING)
                            .description("???????????? ?????????").optional(),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                            .description("?????? ID")
                    )
                )
            )
            .when()
            .port(port)
            .post("/api/v1/auth/signup");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());

        User user = userRepository.findByEmail(dto.getEmail());
        cartRepository.delete(cartRepository.findByUserId(user.getId()).orElseThrow());
        userRepository.delete(userRepository.findByEmail(dto.getEmail()));
    }


    @Test
    @DisplayName("?????? ?????????")
    public void signin() {

        UserDto dto = UserDto.builder().email("test@test.com").password("test").build();

        Response resp = given(this.spec)
            .body(dto)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .filter(document("auth-signin",
                    preprocessRequest(modifyUris()
                            .scheme("https")
                            .host("parabole.com"),
                        prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("?????? ??????").optional(),
                        fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
                            .description("?????? ID").optional(),
                        fieldWithPath("data.token").type(JsonFieldType.STRING).description("????????? ??????").optional(),
                        fieldWithPath("data.email").type(JsonFieldType.STRING)
                            .description("?????????").optional(),
                        fieldWithPath("data.imageUrl").type(JsonFieldType.STRING)
                            .description("????????? url").optional(),
                        fieldWithPath("data.role").type(JsonFieldType.STRING)
                            .description("?????? ??????").optional(),
                        fieldWithPath("data.authProvider").type(JsonFieldType.STRING)
                            .description("?????? ?????????").optional(),
                        fieldWithPath("data.sellerId").type(JsonFieldType.NUMBER)
                            .description("????????? ID").optional(),
                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                            .description("????????????").optional(),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                            .description("?????????").optional(),
                        fieldWithPath("data.phone").type(JsonFieldType.STRING)
                            .description("????????????").optional()
                    )
                )
            )
            .when()
            .port(port)
            .post("/api/v1/auth/signin");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
    }

}
