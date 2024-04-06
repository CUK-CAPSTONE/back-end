    package com.hyunn.capstone.service;

    import com.fasterxml.jackson.core.JsonProcessingException;
    import com.fasterxml.jackson.databind.JsonNode;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.hyunn.capstone.dto.Response.KakaoPayApproveResponse;
    import com.hyunn.capstone.dto.Response.KakaoPayReadyResponse;
    import com.hyunn.capstone.entity.User;
    import com.hyunn.capstone.exception.ApiKeyNotValidException;
    import com.hyunn.capstone.exception.ApiNotFoundException;
    import com.hyunn.capstone.exception.UserNotFoundException;
    import com.hyunn.capstone.repository.UserJpaRepository;

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

        @Value("${spring.security.oauth2.client.kakaoPay.client-secret}")
        private String admin_Key;

        @Value("${spring.security.oauth2.client.kakaoPay.ready-uri}")
        private String readyUrl;

        @Value("${spring.security.oauth2.client.kakaoPay.approve-uri}")
        private String approveUrl;

        private KakaoPayReadyResponse kakaoPayReadyResponse;

        /**
         * 카카오페이 결제준비 단계
         */
        public KakaoPayReadyResponse getReady(String apiKey, String partnerOrderId, String partnerUserId,
                                              String itemName, int quantity, int totalAmount,
                                              String approvalUrl, String cancelUrl, String failUrl)
                throws JsonProcessingException {
            // API KEY 유효성 검사
            if (apiKey == null || !apiKey.equals(xApiKey)) {
                throw new ApiKeyNotValidException("API KEY가 올바르지 않습니다.");
            }


            // 요청 바디를 구성합니다.
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("cid", cid);
            params.add("partner_order_id", partnerOrderId);
            params.add("partner_user_id", partnerUserId);
            params.add("item_name", itemName);
            params.add("quantity", String.valueOf(quantity));
            params.add("total_amount", String.valueOf(totalAmount));
            params.add("approval_url", approvalUrl);
            params.add("cancel_url", cancelUrl);
            params.add("fail_url", failUrl);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
            headers.set("Authorization", "KakaoAK " + admin_Key);
            String requestUrl = readyUrl;

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


        // ... 기존 코드 ...

        /**
         * 카카오페이 결제승인 단계
         */
        public KakaoPayApproveResponse getApprove(String apiKey, String pgToken, String partnerOrderId, String partnerUserId, String tid)
                throws JsonProcessingException {

            // API KEY 유효성 검사
            if (apiKey == null || !apiKey.equals(xApiKey)) {
                throw new ApiKeyNotValidException("API KEY가 올바르지 않습니다.");
            }

            // 요청 헤더를 구성합니다.
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + admin_Key);
            headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            // 승인 요청 바디를 구성합니다.
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("cid", cid);
            params.add("tid", kakaoPayReadyResponse.getTid());
            params.add("partner_order_id", partnerOrderId);
            params.add("partner_user_id", partnerUserId);
            params.add("pg_token", pgToken);

            RestTemplate restTemplate = new RestTemplate();

            // HttpEntity를 생성합니다.
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

            // API 호출을 수행합니다.
            ResponseEntity<String> response = restTemplate.exchange(
                    approveUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // API 응답을 처리합니다.
            if (!response.getStatusCode().is2xxSuccessful()) {
                // API 호출이 실패한 경우의 처리
                throw new ApiNotFoundException("API 호출에 실패했습니다. 상태 코드: " + response.getStatusCode());
            }

            // 성공적으로 API를 호출한 경우의 처리
            String responseBody = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseJson = mapper.readTree(responseBody);


            // KakaoPayApproveResponse 객체를 생성합니다.
            KakaoPayApproveResponse kakaoPayApproveResponse = mapper.treeToValue(responseJson, KakaoPayApproveResponse.class);
            return kakaoPayApproveResponse;
        }
    }
