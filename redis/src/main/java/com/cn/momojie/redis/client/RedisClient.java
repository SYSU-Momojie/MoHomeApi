package com.cn.momojie.redis.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class RedisClient {

	@Autowired
	private LettuceConnectionFactory factory;

	private StringRedisSerializer stringSerializer = new StringRedisSerializer();

	public RedisConnection getConnection() {
		return factory.getConnection();
	}

	public Long del(String key) {
		byte[] k = stringSerializer.serialize(key);
		try (RedisConnection conn = getConnection()) {
			return conn.del(k);
		}
	}

	public Boolean setNX(String key, String value, int seconds) {
		byte[] k = stringSerializer.serialize(key);
		byte[] v = stringSerializer.serialize(value);
		Boolean flag = false;
		try (RedisConnection conn = getConnection()) {
			flag = conn.setNX(k, v);
			if (seconds > 0) {
				conn.expire(k, seconds);
			}
		}

		return flag;
	}
}
