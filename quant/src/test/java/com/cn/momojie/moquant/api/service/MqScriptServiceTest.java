package com.cn.momojie.moquant.api.service;

import com.cn.momojie.moquant.api.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class MqScriptServiceTest extends SpringBaseTest {

    @Autowired
    private MqScriptService service;

    @Test
    public void testRecalculate() throws InterruptedException {
        Map<String, String> m = new HashMap<>();
        m.put("000001.SZ", "20200701");
        service.recalculateFrom(m);
    }
}
