package com.hyunn.capstone.dto.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoPayReadyResponse {
    private String tid = "T1234567890123456789";
    private String next_redirect_mobile_url = "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/mInfo";
    private String next_redirect_pc_url = "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/info";
    private String created_at = "2023-07-15T21:18:22";
}


