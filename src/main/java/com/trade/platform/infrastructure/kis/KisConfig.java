package com.trade.platform.infrastructure.kis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@Slf4j
public class KisConfig {

    @Bean
    public RestClient kisRestClient(
            RestClient.Builder builder,
            KisProperties kisProperties
    ) {
        return builder
                .baseUrl(kisProperties.restBaseUrl())
                .build();
    }
}
