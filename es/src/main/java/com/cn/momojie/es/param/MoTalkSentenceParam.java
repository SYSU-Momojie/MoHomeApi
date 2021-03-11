package com.cn.momojie.es.param;

import org.apache.commons.lang3.StringUtils;

import com.cn.momojie.basic.param.PageParam;

import lombok.Data;

@Data
public class MoTalkSentenceParam extends PageParam {

	private String label;

	private String content;

	private Boolean random;

	public Boolean noLabel() {
		return StringUtils.isEmpty(label) || "全部".equals(label);
	}
}
