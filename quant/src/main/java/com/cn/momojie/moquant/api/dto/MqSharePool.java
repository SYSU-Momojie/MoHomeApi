package com.cn.momojie.moquant.api.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("mq_share_pool")
public class MqSharePool {

    private Long id;

    private String dt;

    private String strategy;

    private String tsCode;

}
