package com.feelmycode.parabole.controller;

import com.feelmycode.parabole.global.api.ParaboleResponse;
import com.feelmycode.parabole.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    /**
     * Amazon S3에 이미지 업로드
     * @return 성공 시 200 Success와 함께 업로드 된 파일의 파일명 리스트 반환
     */
    @PostMapping("/image")
    public ResponseEntity<ParaboleResponse> uploadImage(@RequestPart MultipartFile multipartFile) throws Exception {
        String imgUrl = awsS3Service.upload(multipartFile);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "메세지 업로드", imgUrl);
    }

    /**
     * Amazon S3에 이미지 업로드 된 파일을 삭제
     * @return 성공 시 200 Success
     */
    @DeleteMapping("/image")
    public ResponseEntity<ParaboleResponse> deleteImage(@RequestParam String fileName) {
//        awsS3Service.deleteImage(fileName);
        return ParaboleResponse.CommonResponse(HttpStatus.OK, true, "파일 삭제");
    }
}