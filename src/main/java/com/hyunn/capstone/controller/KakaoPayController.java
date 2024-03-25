package com.hyunn.capstone.controller;

import com.hyunn.capstone.dto.Response.ApiStandardResponse;
import com.hyunn.capstone.dto.Response.ErrorResponse;
import com.hyunn.capstone.dto.Response.KakaoPayApproveResponse;
import com.hyunn.capstone.service.KakaoPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @Operation(summary = "결제 승인 요청",
            description = "결제를 승인하고 결제 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 승인 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KakaoPayApproveResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),


            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "결제 정보를 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 에러",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/approve")
    public ResponseEntity<ApiStandardResponse<KakaoPayApproveResponse>> getPgToken(
            @Parameter(description = "결제 고유 번호", required = true, example = "T1234567890")
            @RequestParam("tid") String tid,
            @Parameter(description = "결제 승인 토큰", required = true, example = "pg_token1234567890")
            @RequestParam("pg_token") String pgToken) {

        KakaoPayApproveResponse response = kakaoPayService.getKakaoPayApprove(pgToken, tid);
        return ResponseEntity.ok(ApiStandardResponse.success(response));
    }
}