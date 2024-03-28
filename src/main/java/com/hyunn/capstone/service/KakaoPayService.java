package com.hyunn.capstone.service;

import com.hyunn.capstone.dto.Request.KakaoPayApproveRequest;
import com.hyunn.capstone.dto.Request.KakaoPayReadyRequest;
import com.hyunn.capstone.dto.Response.KakaoPayApproveResponse;
import com.hyunn.capstone.dto.Response.KakaoPayReadyResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoPayService {

    @Value("${spring.security.oauth2.client.kakaoPay.client-id}")
    private String cid;

    @Value("${spring.security.oauth2.client.kakaoPay.client-secret}")
    private String adminKey;

    @Value("${spring.security.oauth2.client.kakaoPay.ready-uri}")
    private String readyUrl;

    @Value("${spring.security.oauth2.client.kakaoPay.approve-uri}")
    private String approvalUrl;

    public KakaoPayReadyResponse getKaKaoPayReady(KakaoPayReadyRequest request){
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(this.getReadyParameters(request), this.getHeaders());
        try {
            RestTemplate restTemplate = new RestTemplate();
            KakaoPayReadyResponse response = restTemplate.postForObject(
                    readyUrl,
                    requestEntity,
                    KakaoPayReadyResponse.class
            );
            return response;
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(e.getStatusCode(), e.getMessage());
        }
    }

    public KakaoPayApproveResponse getKakaoPayApprove(String pgToken,String tid){
        HttpHeaders headers = this.getHeaders();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid",cid);
        parameters.add("tid",tid);
        parameters.add("pg_token",pgToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.postForObject(approvalUrl, requestEntity, KakaoPayApproveResponse.class);
        } catch (HttpClientErrorException e) {
            throw new HttpClientErrorException(e.getStatusCode(), e.getMessage());
        }
    }


    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = "KakaoAK " + adminKey;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        httpHeaders.set("x-api-key", "rkxhfflreogkrrywkflsrhql2024");
        return httpHeaders;
    }



    private MultiValueMap<String, String> getReadyParameters(KakaoPayReadyRequest request) {

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", request.getPartner_order_id());
        parameters.add("partner_user_id", request.getPartner_user_id());
        parameters.add("item_name", request.getItem_name());
        parameters.add("quantity", request.getQuantity().toString());
        parameters.add("total_amount", String.valueOf(request.getTotal_amount()));
        parameters.add("vat_amount", request.getVat_amount().toString());
        parameters.add("tax_free_amount", request.getTax_free_amount().toString());
        parameters.add("approval_url", approvalUrl);
        parameters.add("cancel_url", request.getCancel_url());
        parameters.add("fail_url", request.getFail_url());

        return parameters;
    }

    private MultiValueMap<String, String> getApproveParameters(KakaoPayApproveRequest request){
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid",cid);
        parameters.add("tid", request.getTid());
        parameters.add("partner_order_id", request.getPartner_order_id());
        parameters.add("user_order_id", request.getPartner_user_id());
        parameters.add("pg_token", request.getPg_token());

        return parameters;
    }


}
