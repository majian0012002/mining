package com.miller.mining.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
//@EnableCaching
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
		factory.setUsePool(true);
		JedisPoolConfig config = getRedisConfig();
		factory.setPoolConfig(config);
		logger.info("JedisConnectionFactory bean init success.");
		return factory;
	}


	@Bean
	@ConfigurationProperties(prefix="spring.redis")
	public RedisTemplate<String,String> getRedisTemplate() {
		StringRedisTemplate template = new StringRedisTemplate(getRedisConnectionFactory());
		//setSerializer(template);
		//template.afterPropertiesSet();
		return template;
	}

	public void setSerializer(StringRedisTemplate template) {
		Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		serializer.setObjectMapper(om);
		template.setValueSerializer(serializer);
	}
}
