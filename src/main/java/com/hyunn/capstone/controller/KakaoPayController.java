package com.hyunn.capstone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hyunn.capstone.dto.Response.ApiStandardResponse;
import com.hyunn.capstone.dto.Response.ImageToTextResponse;
import com.hyunn.capstone.dto.Response.KakaoPayApproveResponse;
import com.hyunn.capstone.dto.Response.KakaoPayReadyResponse;
import com.hyunn.capstone.service.KakaoPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "kakaopay api", description = "카카오페이 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pay")
public class KakaoPayController{

    private final KakaoPayService kakaoPayService;

    @Operation(summary = "tid 생성", description = "request 값을 받아 tid 생성을 통한 결제 준비")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "키워드 반환"),
            @ApiResponse(responseCode = "400",
                    description = "1. 파라미터가 부족합니다. \t\n"
                            + "2. 올바르지 않은 파라미터 값입니다. \t\n"
                            + "3. 올바르지 않은 JSON 형식입니다. \t\n"
                            + "4. 지원하지 않는 형식의 데이터 요청입니다. \t\n",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"code\": \"01\", \"msg\": \"fail\","
                                    + " \"data\": {\"status\": \"INVALID_PARAMETER\", "
                                    + "\"msg\":\"올바르지 않은 파라미터 값입니다.\"} }"))),
            @ApiResponse(responseCode = "404",
                    description = "1. Api 응답이 올바르지 않습니다. \t\n"
                            + "2. S3 업로드가 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"code\": \"10\", \"msg\": \"fail\","
                                    + " \"data\": {\"status\": \"API_NOT_FOUND_EXCEPTION\", "
                                    + "\"msg\":\"Api 응답이 올바르지 않습니다.\"} }")))})
    @Parameter(name = "x-api-key", description = "x-api-key", schema = @Schema(type = "string"),
            in = ParameterIn.HEADER, example = "testapikey1234")
    /**
     * 결제 준비 요청을 처리합니다.
     */
    @PostMapping("/prepare")
    public ResponseEntity<ApiStandardResponse<KakaoPayReadyResponse>> preparePayment(
            @RequestHeader("x-api-key") String apiKey,
            @Parameter(description = "상품명", required = true, example = "powerade")
            @RequestParam("item_name") String itemName,
            @Parameter(description = "총량", required = true, example = "123 (Integer)")
            @RequestParam("total_amount") int totalAmount,
            @Parameter(description = "분량", required = true, example = "1 (Integer)")
            @RequestParam("quantity") int quantity,
            @Parameter(description = "가맹점 주문번호", required = true, example = "partner_order_id")
            @RequestParam("partner_order_id") String partnerOrderId,
            @Parameter(description = "가맹점 회원 id", required = true, example = "partner_user_id")
            @RequestParam("partner_user_id") String partnerUserId,
            @Parameter(description = "결제 성공 시 redirect url", required = true, example = "https://developers.kakao.com/success")
            @RequestParam("approval_url") String approvalUrl,
            @Parameter(description = "결제 취소 시 redirect url", required = true, example = "https://developers.kakao.com/fail")
            @RequestParam("cancel_url") String cancelUrl,
            @Parameter(description = "결제 실패 시 redirect url", required = true, example = "https://developers.kakao.com/cancel")
            @RequestParam("fail_url") String failUrl,
            @Parameter(
                    description = "multipart/form-data 형식의 10MB 이하 이미지 파일을 받습니다.",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestPart("file") MultipartFile file) throws JsonProcessingException {

        KakaoPayReadyResponse response = kakaoPayService.getReady(apiKey, partnerOrderId, partnerUserId, itemName, quantity, totalAmount, approvalUrl, cancelUrl, failUrl);
        return ResponseEntity.ok(ApiStandardResponse.success(response));
    }
    /**
     * 결제 승인 요청을 처리합니다.
     */
    @Operation(summary = "결제 승인", description = "pg_token을 이용한 결제 승인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "키워드 반환"),
            @ApiResponse(responseCode = "400",
                    description = "1. 파라미터가 부족합니다. \t\n"
                            + "2. 올바르지 않은 파라미터 값입니다. \t\n"
                            + "3. 올바르지 않은 JSON 형식입니다. \t\n"
                            + "4. 지원하지 않는 형식의 데이터 요청입니다. \t\n",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"code\": \"01\", \"msg\": \"fail\","
                                    + " \"data\": {\"status\": \"INVALID_PARAMETER\", "
                                    + "\"msg\":\"올바르지 않은 파라미터 값입니다.\"} }"))),
            @ApiResponse(responseCode = "404",
                    description = "1. Api 응답이 올바르지 않습니다. \t\n"
                            + "2. S3 업로드가 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{ \"code\": \"10\", \"msg\": \"fail\","
                                    + " \"data\": {\"status\": \"API_NOT_FOUND_EXCEPTION\", "
                                    + "\"msg\":\"Api 응답이 올바르지 않습니다.\"} }")))})
    @PostMapping("/approve")
    public ResponseEntity<ApiStandardResponse<KakaoPayApproveResponse>> approvePayment(
            @RequestHeader("x-api-key") String apiKey,
            @Parameter(description = "결제 고유 번호(TID)", required = true, example = "T1234567890123456789")
            @RequestParam("tid") String tid,
            @Parameter(description = "가맹점 주문번호", required = true, example = "partner_order_id")
            @RequestParam("partnet_order_id") String partnerOrderId,
            @Parameter(description = "가맹점 회원 id", required = true, example = "partner_user_id")
            @RequestParam("partnerUserId") String partnerUserId,
            @Parameter(description = "결제승인 요청을 인증하는 토큰", required = true, example = "pg_token")
            @RequestParam("pg_token") String pgToken) throws JsonProcessingException {

        KakaoPayApproveResponse response = kakaoPayService.getApprove(apiKey, tid, partnerOrderId, partnerUserId,  pgToken);
        return ResponseEntity.ok(ApiStandardResponse.success(response));
    }
}