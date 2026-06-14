package com.trade.platform.member.domain;

import com.trade.platform.common.exception.MemberException;
import com.trade.platform.common.response.ResponseMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberStatus {
    ENABLE("활성화"),
    DISABLE("비활성화");

    private final String description;

    public static MemberStatus of(String status) {
        if (status == null || status.isBlank()) {
            throw new MemberException(ResponseMessage.INVALID_POSTING_SOURCE_TYPE);
        }

        for (MemberStatus memberStatus : values()) {
            if (status.equals(memberStatus.name())) {
                return memberStatus;
            }
        }

        throw new MemberException(ResponseMessage.INVALID_POSTING_SOURCE_TYPE);
    }
}
