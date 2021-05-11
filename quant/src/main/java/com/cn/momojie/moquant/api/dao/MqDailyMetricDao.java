package com.cn.momojie.moquant.api.dao;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.momojie.moquant.api.dto.MqDailyMetric;
import com.cn.momojie.moquant.api.param.MqShareListParam;

public interface MqDailyMetricDao {

    List<String> getScoreList(MqShareListParam param);

    List<MqDailyMetric> getDailyLatest(@Param("codeList") Collection<String> codeList,
			@Param("nameList") Collection<String> nameList,
			@Param("underDate") String underDate);

    List<MqDailyMetric> getTrend(@Param("code") String tsCode, @Param("name") String name);
}
