package com.cn.momojie.blog.session;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@Configuration
public class SessionConfig {

    public static final String UNIQUE_KEY = "momojie_blog_login";

    public static final String ROLE = "momojie_blog_role";

    public static final String AUTH = "momojie_blog_auth";
}
