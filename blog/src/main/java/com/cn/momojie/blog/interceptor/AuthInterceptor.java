package com.cn.momojie.blog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public abstract class AuthInterceptor extends HandlerInterceptorAdapter {

    protected Boolean noAuthReturn(HttpServletResponse response) throws Exception {
        response.reset();
        response.setStatus(403);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter pw = response.getWriter();
        pw.write("无权限操作");

        pw.flush();
        pw.close();
        return false;
    }
}
