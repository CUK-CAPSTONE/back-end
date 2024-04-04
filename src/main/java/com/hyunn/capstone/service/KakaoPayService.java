package com.hyunn.capstone.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hyunn.capstone.dto.Request.ThreeDimensionCreateRequest;
import com.hyunn.capstone.dto.Response.KakaoPayReadyResponse;
import com.hyunn.capstone.dto.Response.ThreeDimensionCreateResponse;
import com.hyunn.capstone.dto.Response.ThreeDimensionResponse;
import com.hyunn.capstone.entity.Image;
import com.hyunn.capstone.entity.User;
import com.hyunn.capstone.exception.ApiKeyNotValidException;
import com.hyunn.capstone.exception.ApiNotFoundException;
import com.hyunn.capstone.exception.ImageNotFoundException;
import com.hyunn.capstone.exception.UserNotFoundException;
import com.hyunn.capstone.repository.ImageJpaRepository;
import com.hyunn.capstone.repository.UserJpaRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoPayService {

    @Value("${spring.security.x-api-key}")
    private String xApiKey;

    @Value("${spring.security.oauth2.client.kakaoPay.client-id}")
    private String cid;

    @Value("${spring.security.oauth2.client.kakaoPAy.client-secret")
    private String admin_Key;

    @Value("${spring.security.oauth2.client.kakaoPay.ready-uri}")
    private String readyUrl;

    @Value("${spring.security.oauth2.client.kakaoPay.approve-uri}")
    private String approveUrl;

    private final ImageJpaRepository imageJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private KakaoPayReadyResponse kakaoPayReadyResponse;

    /**
     * 카카오페이 결제준비 단계
     */
    public KakaoPayReadyResponse getReady(String apiKey)
            throws JsonProcessingException {
        // API KEY 유효성 검사
        if (apiKey == null || !apiKey.equals(xApiKey)) {
            throw new ApiKeyNotValidException("API KEY가 올바르지 않습니다.");
        }

        Optional<User> rootUser = Optional.ofNullable(userJpaRepository.findById(1L)
                .orElseThrow(() -> new UserNotFoundException("유저 정보를 가져오지 못했습니다.")));


        // 요청 바디를 구성합니다.
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", cid);
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "gorany");
        params.add("item_name", "갤럭시S9");
        params.add("quantity", "1");
        params.add("total_amount", "2100");
        params.add("tax_free_amount", "100");
        params.add("approval_url", "http://localhost:9000/api/pay/success");
        params.add("cancel_url", "http://localhost:9000/api/pay/cancel");
        params.add("fail_url", "http://localhost:9000/api/pay/fail");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        headers.set("Authorization", "KakaoAK " + admin_Key);
        String requestUrl = "https://open-api.kakaopay.com/online/v1/payment/ready";

        // HttpEntity를 생성합니다.
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // API 호출을 수행합니다.
        ResponseEntity<String> response = restTemplate.exchange(
                requestUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // API 응답을 처리합니다.
        if (response.getStatusCode().is2xxSuccessful()) {
            // 성공적으로 API를 호출한 경우의 처리
            System.out.println("API 호출 성공: " + response.getBody());
        } else {
            // API 호출이 실패한 경우의 처리
            System.out.println("API 호출 실패: " + response.getStatusCode());
            throw new ApiNotFoundException("API 호출에 문제가 생겼습니다.");
        }

        // JSON 파싱
        String responseBody = response.getBody();
        if (responseBody == null || responseBody.isEmpty()) {
            throw new ApiNotFoundException("API 응답이 비어 있습니다.");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseJson = mapper.readTree(responseBody);
        String tid = responseJson.get("tid").asText();
        String next_redirect_mobile_url = responseJson.get("next_redirect_mobile_url").asText();
        String next_redirect_pc_url = responseJson.get("next_redirect_pc_url").asText();
        String created_at = responseJson.get("created_at").asText();

        kakaoPayReadyResponse = new KakaoPayReadyResponse(tid, next_redirect_mobile_url, next_redirect_pc_url, created_at);

        return kakaoPayReadyResponse;

    }
}

    /**
     * 카카오페이 결제승인 단계
     */
    /*public KakaoPayReadyResponse getApprove(String apiKey, String previewResult){

        if (apiKey == null || !apiKey.equals(xApiKey)) {
            throw new ApiKeyNotValidException("API KEY가 올바르지 않습니다.");
        }
    }
}*/
