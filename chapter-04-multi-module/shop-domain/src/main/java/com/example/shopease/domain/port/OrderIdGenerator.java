package com.example.shopease.domain.port;

/** Outbound port for order ids, so tests can use predictable ids. */
@FunctionalInterface
public interface OrderIdGenerator {

    String nextId();
}
