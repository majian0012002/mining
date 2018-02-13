package com.miller.mining.config;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableAutoConfiguration
public class RedisCacheConfig {

	private Logger logger = Logger.getLogger(RedisCacheConfig.class);
	
	@Bean
	@ConfigurationProperties(prefix="spring.redis")
	public JedisPoolConfig getRedisConfig() {
		JedisPoolConfig redisConfig = new JedisPoolConfig();
		return redisConfig;
	}
	
	@Bean
	@ConfigurationProperties(prefix="spring.redis")
	public JedisConnectionFactory getRedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		JedisPoolConfig config = getRedisConfig();
		factory.setPoolConfig(config);
		logger.info("JedisConnectionFactory bean init success.");
		return factory;
	}
	
	public RedisTemplate<String,?> getRedisTemplate() {
		RedisTemplate<String,?> template = new StringRedisTemplate(getRedisConnectionFactory());
		return template;
	}
}
