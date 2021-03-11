package com.cn.momojie.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cn.momojie.auth.SessionUtil;
import com.cn.momojie.auth.session.SessionConfig;
import com.cn.momojie.auth.session.SessionOpService;
import com.cn.momojie.auth.session.UserInfo;

@Component
public class UserInitInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SessionOpService sessionOpService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserInfo info = new UserInfo();
        if (sessionOpService.contains(SessionConfig.UNIQUE_KEY)) {
            info.setLogin(true);
            info.setName(getUserName());
            info.setRoles(getUserRoles());
            info.setAuths(getUserAuths());
        }

        SessionUtil.init(info);

        return true;
    }

    private String getUserName() {
        return sessionOpService.get(SessionConfig.UNIQUE_KEY, String.class);
    }

    private List<String> getUserRoles() {
        return sessionOpService.getList(SessionConfig.ROLE, String.class);
    }

    private List<String> getUserAuths() {
        return sessionOpService.getList(SessionConfig.AUTH, String.class);
    }
}
