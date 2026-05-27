package com.Graxa_API.Graxa_API.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory cf, ObjectMapper objectMapper) {

        // Cache em memória exclusivo para "users" (auth) — nunca vai ao Redis,
        // sempre começa limpo no restart e evita problemas de serialização de dados de segurança
        ConcurrentMapCacheManager inMemory = new ConcurrentMapCacheManager();
        inMemory.setCacheNames(List.of("users"));

        // Redis cache para dados da aplicação (bandas, etc.)
        ObjectMapper redisMapper = objectMapper.copy();
        redisMapper.activateDefaultTypingAsProperty(
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType(Object.class)
                        .build(),
                ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE,
                "@class"
        );

        Jackson2JsonRedisSerializer<Object> serializer =
                new Jackson2JsonRedisSerializer<>(redisMapper, Object.class);

        var defaults = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .disableCachingNullValues()
                .prefixCacheNameWith("graxa::")
                .entryTtl(Duration.ofMinutes(5));

        var perCache = Map.of(
                "bandas", defaults.entryTtl(Duration.ofMinutes(10))
        );

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(cf)
                .cacheDefaults(defaults)
                .withInitialCacheConfigurations(perCache)
                .build();

        // CompositeCacheManager: verifica in-memory primeiro, depois Redis.
        // "users" é resolvido pelo inMemory; "bandas" cai no Redis.
        return new CompositeCacheManager(inMemory, redisCacheManager);
    }
}
