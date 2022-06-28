package com.nttdata.microservices.bootcoin.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.microservices.bootcoin.service.dto.WalletDto;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.ReactiveKeyCommands;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveStringCommands;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@EnableCaching
@Configuration
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
public class RedisConfiguration {

  @Value("${spring.application.name}")
  private String name;

  @Bean
  public CacheManager redisCacheManager(ObjectMapper objectMapper,
                                        RedisConnectionFactory connectionFactory) {
    RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig();

    cacheConfig.entryTtl(Duration.ofMinutes(60));
    cacheConfig.prefixCacheNameWith(name);
    cacheConfig.serializeKeysWith(RedisSerializationContext
        .SerializationPair.fromSerializer(RedisSerializer.string()));
    cacheConfig.serializeValuesWith(RedisSerializationContext
        .SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));
    return RedisCacheManager.builder()
        .cacheWriter(RedisCacheWriter.lockingRedisCacheWriter(connectionFactory))
        .cacheDefaults(cacheConfig)
        .build();
  }


  @Bean
  public ReactiveHashOperations<String, String, WalletDto> hashOperations(
      ReactiveRedisConnectionFactory redisConnectionFactory) {
    var template = new ReactiveRedisTemplate<>(
        redisConnectionFactory,
        RedisSerializationContext.<String, WalletDto>newSerializationContext(
                new StringRedisSerializer())
            .hashKey(new GenericToStringSerializer<>(Integer.class))
            .hashValue(new Jackson2JsonRedisSerializer<>(WalletDto.class))
            .build()
    );
    return template.opsForHash();
  }

  @Bean
  public ReactiveRedisTemplate<String, WalletDto> reactiveRedisTemplate(
      ReactiveRedisConnectionFactory factory) {
    Jackson2JsonRedisSerializer<WalletDto> serializer =
        new Jackson2JsonRedisSerializer<>(WalletDto.class);
    RedisSerializationContext.RedisSerializationContextBuilder<String, WalletDto> builder =
        RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
    RedisSerializationContext<String, WalletDto> context = builder.value(serializer)
        .build();
    return new ReactiveRedisTemplate<>(factory, context);
  }

  @Bean
  public ReactiveRedisTemplate<String, String> reactiveRedisTemplateString(
      ReactiveRedisConnectionFactory connectionFactory) {
    return new ReactiveRedisTemplate<>(connectionFactory, RedisSerializationContext.string());
  }

  @Bean
  public ReactiveKeyCommands keyCommands(
      final ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
    return reactiveRedisConnectionFactory.getReactiveConnection()
        .keyCommands();
  }

  @Bean
  public ReactiveStringCommands stringCommands(
      final ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
    return reactiveRedisConnectionFactory.getReactiveConnection()
        .stringCommands();
  }


}
