package com.cn.momojie.moquant.api.vo.strategy;

import com.cn.momojie.basic.vo.PageResult;
import com.cn.momojie.moquant.api.dto.MqShareAll;
import lombok.Data;

@Data
public class MqSharePool {

    private String strategy;

    private String dt;

    private PageResult<MqShareAll> pool;
}
