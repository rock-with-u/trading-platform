package com.trade.platform.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IdempotencyKeyGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final TimeProvider timeProvider;

    public String generate(IdempotencyKeyPrefix prefix) {
        return prefix.getCode() + "-" +
                timeProvider.now().format(DATE_FORMATTER) +
                "-" +
                UUID.randomUUID().toString().replace("-", "");
    }
}
