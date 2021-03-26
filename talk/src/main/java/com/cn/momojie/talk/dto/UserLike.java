package com.cn.momojie.talk.dto;

import lombok.Data;

@Data
public class UserLike {

	private String sentenceType;

	private Long userId;

	private String sentenceId;

	private Integer like;

	private Integer dislike;
}
