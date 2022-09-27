package com.feelmycode.parabole.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.service.ProductService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs
public class ProductControllerTest {

    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;
    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
            .apply(documentationConfiguration(this.restDocumentation))       // (2)
            .build();
    }

    @Test
    @DisplayName("상품 목록 조회")
    public void getProductList() throws Exception{
        this.mockMvc.perform(get("/api/v1/product/list"))
            .andDo(print())
            .andDo(document("product-list",
                preprocessResponse(prettyPrint())))
            .andExpect(status().isOk());
    }

    @Test
    public void createProduct() throws Exception {
        Product product = new Product(1L, 1L, 1, 50L, "국밥", "https://img.url", "순대국밥", 2000L);
        this.mockMvc.perform(post("/api/v1/product")
                .content(String.valueOf(MediaType.APPLICATION_JSON))
            .content(this.objectMapper.writeValueAsString(product)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("product",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()))
            );
    }

}