package com.hyunn.capstone.dto.Request;

import lombok.Getter;

@Getter
public class KakaoPayApproveRequest {
    private String cid;
    private String tid;
    private String partner_order_id;
    private String partner_user_id;
    private String pg_token;
}
