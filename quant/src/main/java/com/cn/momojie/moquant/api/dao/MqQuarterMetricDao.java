package com.cn.momojie.moquant.api.dao;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.momojie.moquant.api.dto.MqQuarterMetric;

public interface MqQuarterMetricDao {

	List<MqQuarterMetric> getQuarterLatest(@Param("codeList") Collection<String> codeList,
			@Param("nameList") Collection<String> nameList,
			@Param("underDate") String underDate,
			@Param("lastYear") String lastYear);

	List<MqQuarterMetric> getTrend(@Param("code") String tsCode, @Param("name") String name);
}
