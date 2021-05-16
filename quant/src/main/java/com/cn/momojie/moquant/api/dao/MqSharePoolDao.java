package com.cn.momojie.moquant.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.momojie.moquant.api.dto.MqSharePool;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MqSharePoolDao extends BaseMapper<MqSharePool> {

    List<MqSharePool> getLatestByStrategy(@Param("strategy") String strategy);
}
