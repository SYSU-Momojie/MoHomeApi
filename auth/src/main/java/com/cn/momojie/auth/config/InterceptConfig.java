package com.cn.momojie.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cn.momojie.auth.interceptor.AdminInterceptor;
import com.cn.momojie.auth.interceptor.UserInitInterceptor;

@Configuration
public class InterceptConfig implements WebMvcConfigurer {

	@Autowired
	private AdminInterceptor adminI;

	@Autowired
	private UserInitInterceptor userInitI;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration admin = registry.addInterceptor(adminI);
		admin.addPathPatterns("/gallery/upload", "/article/post");
		admin.order(1);

		InterceptorRegistration init = registry.addInterceptor(userInitI);
		init.addPathPatterns("/**");
		init.order(0);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		if ("dev".equals(System.getenv("SPRING_PROFILE_ACTIVE"))) {
			registry.addMapping("/**")
					.allowedOrigins("http://localhost:9876")
					.allowCredentials(true);
		}
	}
}
