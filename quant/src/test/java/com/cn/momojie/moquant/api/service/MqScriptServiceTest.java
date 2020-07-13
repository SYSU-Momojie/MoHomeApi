package com.cn.momojie.moquant.api.service;

import org.junit.Test;

public class MqScriptServiceTest {

    private MqScriptService getService() {
        MqScriptService service = new MqScriptService();
        return service;
    }

    @Test
    public void testRecalculate() {
        MqScriptService service = getService();
        String path = System.getenv("MQ_SCRIPT_DIR");
        service.recalculateFrom(path, "000001.SZ", "20200701");
    }
}
