package com.cn.momojie.blog;

import com.cn.momojie.blog.session.UserInfo;

public class SessionUtil {

    private static final ThreadLocal<UserInfo> USER_INFO = new ThreadLocal<>();

    public static void init(UserInfo info) {
        USER_INFO.set(info);
    }

    public static String getUserName() {
        UserInfo info = USER_INFO.get();
        return info == null ? "" : info.getName();
    }

    public static Boolean isLogin() {
        UserInfo info = get();
        return info != null && info.getLogin();
    }

    public static UserInfo get() {
        return USER_INFO.get();
    }

    public static Boolean isAdmin() {
        UserInfo info = get();
        return info != null && info.getRoles().contains("admin");
    }

    public static Boolean hasAuth(String auth) {
        UserInfo info = get();
        return info != null && info.getAuths().contains(auth);
    }
}
