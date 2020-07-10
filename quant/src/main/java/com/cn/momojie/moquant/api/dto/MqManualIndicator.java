package com.cn.momojie.moquant.api.dto;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("mq_manual_indicator")
public class MqManualIndicator {

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	@TableField("ts_code")
	private String tsCode;

	@TableField("update_date")
	private String updateDate;

	@TableField("period")
	private String period;

	@TableField("report_type")
	private Integer reportType;

	@TableField("name")
	private String name;

	@TableField("value")
	private BigDecimal value;
}
