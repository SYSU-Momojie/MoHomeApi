package com.cn.momojie.moquant.api.param;

import com.cn.momojie.basic.param.PageParam;

import lombok.Data;

@Data
public class CodePageParam extends PageParam {

	private String tsCode;
}
