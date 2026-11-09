package com.example.shopease.infrastructure.id;

import com.example.shopease.domain.port.OrderIdGenerator;
import java.util.concurrent.atomic.AtomicLong;

/** Human-friendly ids like ORD-000001. A real system might use a DB sequence instead. */
public final class SequentialOrderIdGenerator implements OrderIdGenerator {

    private final AtomicLong counter = new AtomicLong();

    @Override
    public String nextId() {
        return String.format("ORD-%06d", counter.incrementAndGet());
    }
}
