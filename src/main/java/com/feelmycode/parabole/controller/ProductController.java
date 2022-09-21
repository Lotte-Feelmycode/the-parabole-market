package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.Message;
import com.feelmycode.parabole.domain.Product;
import com.feelmycode.parabole.service.ProductService;
import java.nio.charset.Charset;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity createProduct(@RequestBody Product product) {
        productService.saveProduct(product);
        Message message = new Message();
        message.setMessage("테스트 메세지 전송");
        message.setSuccess(true);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, header, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);

        Message message = new Message();
        message.setMessage("테스트 메세지 전송");
        message.setSuccess(true);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, header, HttpStatus.OK);
   }



}
