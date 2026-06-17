package com.trade.platform.infrastructure.kis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KisApprovalKeyRefreshService {

    private final KisApprovalKeyClient approvalKeyClient;
    private final KisApprovalKeyRepository approvalKeyRepository;

    @Retryable(
            retryFor = {
                    ResourceAccessException.class,
                    HttpServerErrorException.class,
            },
            maxAttempts = 4,
            backoff = @Backoff(
                    delay = 1_000,
                    multiplier = 2.0,
                    maxDelay = 10_000
            )
    )
    public void refresh() {
        String approvalKey = approvalKeyClient.issueApprovalKey();

        approvalKeyRepository.save(approvalKey);

        log.info("KIS WebSocket 접속키 갱신 완료");
    }

    @Recover
    public void recover(
            Exception exception
    ) {
        log.error("KIS WebSocket 접속키 갱신이 최종 실패했습니다.", exception);
    }
}
