package com.trade.platform.infrastructure.kis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KisApprovalKeyDto {

    public record KisApprovalKeyRequest(
            @JsonProperty("grant_type")
            String grantType,

            @JsonProperty("appkey")
            String appKey,

            @JsonProperty("secretkey")
            String secretKey
    ) {
        public static KisApprovalKeyRequest of(
                String appKey,
                String appSecret
        ) {
            return new KisApprovalKeyRequest(
                    "client_credentials",
                    appKey,
                    appSecret
            );
        }
    }

    public record KisApprovalKeyResponse(
            @JsonProperty("approval_key")
            String approvalKey
    ) {
    }
}
