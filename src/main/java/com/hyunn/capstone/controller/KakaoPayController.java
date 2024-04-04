package com.hyunn.capstone.controller;

import com.hyunn.capstone.dto.Response.ApiStandardResponse;
import com.hyunn.capstone.dto.Response.ErrorResponse;
import com.hyunn.capstone.dto.Response.KakaoPayApproveResponse;
import com.hyunn.capstone.service.KakaoPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pay")
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
    })
    @Parameter(name = "x-api-key", description = "x-api-key", schema = @Schema(type = "string"),
            in = ParameterIn.HEADER, example = "testapieky1234")
    @GetMapping("/approve")
    public ResponseEntity<ApiStandardResponse<KakaoPayApproveResponse>> getPgToken(
            @RequestHeader(value = "x-api-key", required = false) String apiKey,
            @Parameter(description = "결제 고유 번호", required = true, example = "T1234567890")
            @RequestParam("tid") String tid,
            @Parameter(description = "결제 승인 토큰", required = true, example = "pg_token1234567890")
            @RequestParam("pg_token") String pgToken) {

        KakaoPayApproveResponse response = kakaoPayService.getKakaoPayApprove(apiKey, pgToken, tid);
        return ResponseEntity.ok(ApiStandardResponse.success(response));
    }
}