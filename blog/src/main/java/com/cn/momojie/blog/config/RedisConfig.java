package com.cn.momojie.blog.config;

import lombok.Data;

@Data
public class RedisConfig {

	private String hostName;

	private Integer port;

	private String password;
}
