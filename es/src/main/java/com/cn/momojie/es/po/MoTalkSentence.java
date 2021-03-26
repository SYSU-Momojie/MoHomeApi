package com.cn.momojie.es.po;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Data
@Document(indexName = "motalk_sentence")
public class MoTalkSentence {

	@Id
	private String id;

	private String content;

	private List<String> labels;

	private Integer like;

	private Integer dislike;
}
