package com.cn.momojie.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.cn.momojie.auth.SessionUtil;
import com.cn.momojie.auth.session.SessionConfig;
import com.cn.momojie.auth.session.SessionOpService;
import com.cn.momojie.auth.session.UserInfo;

@Component
public class UserInitInterceptor implements HandlerInterceptor {

    @Autowired
    private SessionOpService sessionOpService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		UserInfo ui = new UserInfo();
        if (sessionOpService.contains(SessionConfig.USER_KEY)) {
			ui = sessionOpService.get(SessionConfig.USER_KEY, UserInfo.class);
			ui.setLogin(true);
        }

        SessionUtil.init(ui);

        return true;
    }
}
