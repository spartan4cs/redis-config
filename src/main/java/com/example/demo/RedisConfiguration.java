package com.example.demo;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisPoolingClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ObjectUtils;

@Configuration
public class RedisConfiguration {

	@Value("${redis.connect.timout.insecs}")
	long connectionTimeout = 0;

	@Value("${redis.host}")
	private String host;

	@Value("${redis.password}")
	private String password;

	@Value("${redis.port}")
	private String port;

	@Value("${redis.read.timout.insecs}")
	private long readTimoutInSec;

	@Value("${redis.max-wait}")
	int maxTotal;
	@Value("${redis.max-idle}")
	int maxIdle;
	@Value("${redis.min.idle}")
	int minIdle;
	@Value("${redis.max-total}")
	long maxWaitMillis;

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(getJedisConnectionFactory());

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		return redisTemplate;

	}

	@Bean
	public JedisConnectionFactory getJedisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		if (ObjectUtils.isEmpty(password)) {
			redisStandaloneConfiguration.setPassword(password);
		}
		redisStandaloneConfiguration.setPassword(password);
		return new JedisConnectionFactory(redisStandaloneConfiguration, getJedisCleanConfiguration());
	}

	@Bean
	public JedisClientConfiguration getJedisCleanConfiguration() {

		JedisPoolingClientConfigurationBuilder jedisPoolingClientConfigurationBuilder = (JedisPoolingClientConfigurationBuilder) JedisClientConfiguration
				.builder().readTimeout(Duration.ofSeconds(readTimoutInSec))
				.connectTimeout(Duration.ofSeconds(connectionTimeout));

		GenericObjectPoolConfig genericObjectPollConfig = new GenericObjectPoolConfig<>();

		genericObjectPollConfig.setMaxTotal(maxTotal);

		genericObjectPollConfig.setMaxIdle(maxIdle);

		genericObjectPollConfig.setMinIdle(minIdle);

		genericObjectPollConfig.setMaxWaitMillis(maxWaitMillis);
		return jedisPoolingClientConfigurationBuilder.poolConfig(genericObjectPollConfig).build();

	}
}
