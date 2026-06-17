package com.trade.platform.infrastructure.kis;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.trade.platform.infrastructure.redis.RedisDistributedLockManager;
import com.trade.platform.infrastructure.redis.RedisLockToken;
import java.time.Duration;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KisApprovalKeyScheduler {

    private static final String APPROVAL_LOCK_KEY = "lock:kis:approval-key:refresh";
    private static final Duration LOCK_LEASE_TIME = Duration.ofMinutes(2);

    private final RedisDistributedLockManager lockManager;
    private final KisApprovalKeyRefreshService refreshService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void refreshApprovalKey() {
        Optional<RedisLockToken> lockToken = lockManager.tryLock(APPROVAL_LOCK_KEY, LOCK_LEASE_TIME);

        if (lockToken.isEmpty()) {
            log.info("KIS Approval Key 갱신 생략 - 다른 인스턴스에서 실행 중");
            return;
        }

        RedisLockToken acquiredLock = lockToken.get();

        try {
            log.info("KIS Approval Key 갱신 분산락 획득");
            refreshService.refresh();
        } finally {
            boolean released = lockManager.unlock(acquiredLock);

            if (released) {
                log.debug("KIS Approval Key 갱신 분산락 해제");
            } else {
                log.warn("KIS Approval Key 갱신 분산락 해제 실패 또는 만료");
            }
        }
    }
}
