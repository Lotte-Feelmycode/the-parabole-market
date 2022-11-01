package com.feelmycode.parabole.security.model;

import lombok.Data;

@Data
public class NaverProfile {

    public String resultcode;
    public String message;
    public Response response;

    @Data
    public class Response {
        public String id;
        public String nickname;
        public String email;
        public String profile_image;
        public String mobile;

//        public String mobile_e164;
//        public String age;
//        public String name;
//        public String gender;
//        public String birthday;
//        public String birthyear;
    }
}

//문서에서 준 Response 였으나 실제결과와 달랐음 띠용용//{
//    "resultcode": "00",
//    "message": "success",
//    "response": {
//    "email": "openapi@naver.com",
//    "nickname": "OpenAPI",
//    "profile_image": "https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif",
//    "age": "40-49",
//    "gender": "F",
//    "id": "32742776",
//    "name": "오픈 API",
//    "birthday": "10-01",
//    "birthyear": "1900",
//    "mobile": "010-0000-0000"
//    }
//}