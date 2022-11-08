package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.domain.ProductDetail;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.AwsS3Service;
import com.feelmycode.parabole.service.ProductDetailService;
import com.feelmycode.parabole.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/v1/s3")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;
    private final ProductDetailService productDetailService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ParaboleResponse> uploadImage(@RequestParam() Long productId,
        @RequestPart("images") List<MultipartFile> multipartFile) throws Exception {
        if (multipartFile != null) {
            for (int i = 0; i < multipartFile.size(); i++) {
                MultipartFile file = multipartFile.get(i);
                String imgUrl = awsS3Service.upload(file);
                if(i == 0) {
                    productService.updateProductThumbnailImg(productId, imgUrl);
                } else {
                    productDetailService.createProductDetail(
                        1L, new ProductDetail(productService.getProduct(productId), imgUrl, ""));
                }
            }
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이미지 업로드");
    }

}
