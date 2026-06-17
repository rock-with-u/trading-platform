package com.trade.platform.common;

import java.time.ZoneId;
import org.springframework.context.annotation.Configuration;
import java.time.Clock;
import org.springframework.context.annotation.Bean;

@Configuration
public class TimeConfig {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Asia/Seoul"));
    }
}
