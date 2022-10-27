package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.dto.ProductDetailRequestDto;
import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.AwsS3Service;
import com.feelmycode.parabole.service.ProductDetailService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    /**
     * Amazon S3에 이미지 업로드
     *
     * @return 성공 시 200 Success와 함께 업로드 된 파일의 파일명 리스트 반환
     */
    @PostMapping
    public ResponseEntity<ParaboleResponse> uploadImage(@RequestParam() Long productId,
        @RequestPart("images") List<MultipartFile> multipartFile) throws Exception {
        log.info("productId: {}", productId);

        log.info("UPLOAD IMAGE size: {}", multipartFile == null ? 0 : multipartFile.size());
//        log.info("UPLOAD IMAGE: {}", multipartFile);
        if (multipartFile != null) {
            for (MultipartFile file : multipartFile) {
                String imgUrl = awsS3Service.upload(file);
                productDetailService.createProductDetail(
                    new ProductDetailRequestDto(productId, imgUrl, "").toDto());
            }
        }
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "이미지 업로드");
    }

    /**
     * Amazon S3에 이미지 업로드 된 파일을 삭제
     *
     * @return 성공 시 200 Success
     */
    @DeleteMapping
    public ResponseEntity<ParaboleResponse> deleteImage(@RequestParam String fileName) {
//        awsS3Service.deleteImage(fileName);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "파일 삭제");
    }
}