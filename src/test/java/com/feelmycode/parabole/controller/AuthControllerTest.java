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

    String outputDirectory = "./src/docs/asciidoc/snippets";

    @LocalServerPort
    int port;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(outputDirectory);

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
    @DisplayName("회원가입")
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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보"),
                        fieldWithPath("data.token").type(JsonFieldType.STRING).description("사용자 토큰")
                            .optional(),
                        fieldWithPath("data.email").type(JsonFieldType.STRING)
                            .description("이메일"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                            .description("사용자명"),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                            .description("닉네임").optional(),
                        fieldWithPath("data.phone").type(JsonFieldType.STRING)
                            .description("휴대전화").optional(),
                        fieldWithPath("data.password").type(JsonFieldType.STRING)
                            .description("비밀번호").optional(),
                        fieldWithPath("data.passwordConfirmation").type(JsonFieldType.STRING)
                            .description("비밀번호 재확인").optional(),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                            .description("유저 ID")
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
    @DisplayName("기본 로그인")
    public void signin() {

        User newUser = userRepository.save(
            new User().builder().email("testest@naver.com").password("test").build());
        UserDto dto = UserDto.builder().email(newUser.getEmail()).password(newUser.getPassword()).build();

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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("메세지"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 정보").optional(),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                            .description("유저 ID").optional(),
                        fieldWithPath("data.token").type(JsonFieldType.STRING).description("사용자 토큰").optional(),
                        fieldWithPath("data.email").type(JsonFieldType.STRING)
                            .description("이메일").optional(),
                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                            .description("사용자명").optional(),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                            .description("닉네임").optional(),
                        fieldWithPath("data.phone").type(JsonFieldType.STRING)
                            .description("휴대전화").optional(),
                        fieldWithPath("data.password").type(JsonFieldType.STRING)
                            .description("비밀번호").optional(),
                        fieldWithPath("data.passwordConfirmation").type(JsonFieldType.STRING)
                            .description("비밀번호 재확인").optional()
                    )
                )
            )
            .when()
            .port(port)
            .post("/api/v1/auth/signin");

        // Then
        Assertions.assertEquals(HttpStatus.OK.value(), resp.statusCode());
        userRepository.delete(newUser);
    }

}
