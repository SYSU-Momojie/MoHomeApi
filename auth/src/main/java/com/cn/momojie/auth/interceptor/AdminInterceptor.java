package com.cn.momojie.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.cn.momojie.auth.SessionUtil;

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
