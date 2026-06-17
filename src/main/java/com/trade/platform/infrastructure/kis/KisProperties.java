package com.trade.platform.infrastructure.kis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kis")
public record KisProperties(
        String appKey,
        String appSecret,
        String restBaseUrl
) {
}
