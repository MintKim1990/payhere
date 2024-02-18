package com.homework.payhere.test.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.boot.test.context.TestConfiguration
import redis.embedded.RedisServer

@TestConfiguration
class EmbeddedRedisConfig(
    private val redisServer: RedisServer = RedisServer(63790)
) {

    @PostConstruct
    fun start() {
        try {
            redisServer.start()
        } catch (e : Exception) { }
    }

    @PreDestroy
    fun stop() {
        redisServer.stop()
    }

}