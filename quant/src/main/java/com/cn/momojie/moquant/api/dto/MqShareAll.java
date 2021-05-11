package com.cn.momojie.moquant.api.dto;

import lombok.Data;

import java.util.Map;

@Data
public class MqShareAll {

	private String tsCode;

	private String shareName;

	private Map<String, MqDailyMetric> dailyMetric;

	private Map<String, MqQuarterMetric> quarterMetric;
}