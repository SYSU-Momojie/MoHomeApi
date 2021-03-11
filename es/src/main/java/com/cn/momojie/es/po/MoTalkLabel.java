package com.cn.momojie.es.po;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Data
@Document(indexName = "motalk_label")
public class MoTalkLabel {

	@Id
	private String id;

	private String label;

}
