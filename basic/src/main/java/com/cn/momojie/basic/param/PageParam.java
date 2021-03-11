package com.cn.momojie.basic.param;

import lombok.Data;

@Data
public class PageParam {

	private Integer pageNum = 1;

	private Integer pageSize = 20;
}
