package com.cn.momojie.moquant.api.param;

import lombok.Data;

@Data
public class DailyBasicParam extends CodePageParam {

    private Boolean orderByDate = false;

    private String dt;

    private String forTrend;
}
