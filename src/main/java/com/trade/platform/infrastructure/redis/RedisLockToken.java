package com.trade.platform.infrastructure.redis;

public record RedisLockToken(
        String key,
        String ownerId
) {
}
