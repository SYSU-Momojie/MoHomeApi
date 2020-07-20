package com.cn.momojie.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@Slf4j
public class LettuceConfig {

	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory(@Autowired RedisConfig redisConfig) {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisConfig.getHostName(), redisConfig.getPort());
		if (redisConfig.getPassword() != null && redisConfig.getPassword().length() > 0) {
			config.setPassword(RedisPassword.of(redisConfig.getPassword()));
		}
		return new LettuceConnectionFactory(config);
	}
}
