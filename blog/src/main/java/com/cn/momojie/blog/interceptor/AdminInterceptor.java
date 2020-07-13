package com.cn.momojie.blog.interceptor;

import com.cn.momojie.blog.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class AdminInterceptor extends AuthInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (SessionUtil.isAdmin()) {
			return true;
		} else {
			return noAuthReturn(response);
		}
	}

}
