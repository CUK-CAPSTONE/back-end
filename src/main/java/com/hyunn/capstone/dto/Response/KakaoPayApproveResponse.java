package com.hyunn.capstone.dto.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoPayApproveResponse {
    private String aid; // 요청 고유 번호
    private String tid; // 결제 고유 번호
    private String cid; // 가맹점 코드
    private String sid; // 정기결제용 ID
    private String partner_order_id; // 가맹점 주문 번호
    private String partner_user_id; // 가맹점 회원 id
    private String payment_method_type; // 결제 수단
    private Amount amount; // 결제 금액 정보
    @Getter
    @Setter
    @ToString
    public static class Amount {
        private Integer total;        // 전체 결제 금액
        private Integer tax_free;     // 비과세 금액
        private Integer vat;          // 부가세 금액
        private Integer point;        // 사용한 포인트 금액
        private Integer discount;     // 할인 금액
        private Integer green_deposit; // 친환경 보증금
    }
    private String item_name; // 상품명
    private String item_code; // 상품 코드
    private int quantity; // 상품 수량
    private String created_at; // 결제 요청 시간
    private String approved_at; // 결제 승인 시간
    private String payload; // 결제 승인 요청에 대해 저장 값, 요청 시 전달 내용
}
