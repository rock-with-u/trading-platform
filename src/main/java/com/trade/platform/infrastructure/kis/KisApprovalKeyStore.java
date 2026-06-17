package com.trade.platform.infrastructure.kis;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KisApprovalKeyStore implements KisApprovalKeyRepository {

    private static final String APPROVAL_KEY = "kis:approval-key";
    private static final Duration EXPIRATION = Duration.ofHours(24);

    private final StringRedisTemplate redisTemplate;

    public void save(String approvalKey) {
        redisTemplate
                .opsForValue()
                .set(APPROVAL_KEY, approvalKey, EXPIRATION);
    }
}
