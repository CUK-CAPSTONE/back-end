package com.hyunn.capstone.controller;

import com.hyunn.capstone.dto.Response.KakaoPayApproveResponse;
import com.hyunn.capstone.service.KakaoPayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    public KakaoPayController(KakaoPayService kakaoPayService) {
        this.kakaoPayService = kakaoPayService;
    }

    @GetMapping("/approve")
    public ResponseEntity <KakaoPayApproveResponse> getPgToken(
            @RequestParam("pg_token") String pgToken,
            @RequestParam("tid") String tid){
        //KakaoPayApproveResponse response = kakaoPayService.getKakaoPayApprove(pgToken);
        //return ResponseEntity.ok(response);
        KakaoPayApproveResponse response = kakaoPayService.getKakaoPayApprove(pgToken, tid);
        return ResponseEntity.ok(response);
    }
}
