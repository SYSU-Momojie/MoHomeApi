package com.cn.momojie.moquant.api.constant;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class MqExcelConst {

	public static final Map<String, String> MANUAL_FIELDS = ImmutableMap.<String, String>builder()
			.put("tsCode", "TS代码")
			.put("updateDate", "更新日期")
			.put("period", "报告期")
			.put("reportType", "报告类型")
			.put("name", "指标")
			.put("value", "值")
			.build();

}
