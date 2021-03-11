package com.cn.momojie.auth.session;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@Slf4j
public class SessionOpService {

    @Autowired
    private HttpSession session;

    public void write(String key, Object value) {
        String jsonStr = JSON.toJSONString(value);
        log.debug("Session value {}: {}", key, session.getAttribute(key));
        session.setAttribute(key, jsonStr);
    }

    public void remove(String key) {
        session.removeAttribute(key);
        log.debug("Session value after removal {}: {}", key, session.getAttribute(key));
    }

    public Boolean contains(String key) {
        return CollectionUtils.contains(session.getAttributeNames(), key);
    }

    public void invalidate() {
        session.invalidate();
    }

    public <T> T get(String key, Class<T> clazz) {
        String strValue = (String)session.getAttribute(key);
        T value = JSON.parseObject(strValue, clazz);
        return value;
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        String strValue = (String)session.getAttribute(key);
        List<T> list = JSON.parseArray(strValue).toJavaList(clazz);
        return list;
    }
}