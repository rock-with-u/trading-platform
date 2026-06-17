package com.trade.platform.infrastructure.redis;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisDistributedLockManager {

    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT = new DefaultRedisScript<>(
            """
                    if redis.call('get', KEYS[1]) == ARGV[1] then
                        return redis.call('del', KEYS[1])
                    end
                    
                    return 0
                    """,
            Long.class
    );

    private final StringRedisTemplate redisTemplate;

    public Optional<RedisLockToken> tryLock(String lockKey, Duration leaseTime) {
        String ownerId = UUID.randomUUID().toString();

        Boolean acquired = redisTemplate.opsForValue()
                .setIfAbsent(
                        lockKey,
                        ownerId,
                        leaseTime
                );

        if (!Boolean.TRUE.equals(acquired)) {
            return Optional.empty();
        }

        return Optional.of(new RedisLockToken(lockKey, ownerId));
    }

    public boolean unlock(RedisLockToken lockToken) {
        Long result = redisTemplate.execute(
                UNLOCK_SCRIPT,
                List.of(lockToken.key()),
                lockToken.ownerId()
        );

        return Long.valueOf(1L).equals(result);
    }
}
