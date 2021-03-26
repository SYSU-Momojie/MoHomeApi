package com.cn.momojie.talk.vo;

import java.util.List;

import com.cn.momojie.es.po.MoTalkSentence;
import com.cn.momojie.talk.dto.UserLike;

import lombok.Data;

@Data
public class SentenceVo {

	private String id;

	private String content;

	private List<String> labels;

	private Integer like;

	private Integer dislike;

	private Boolean likeByUser = false;

	private Boolean dislikeByUser = false;

	public static SentenceVo fromSentence(MoTalkSentence s, UserLike like) {
		SentenceVo vo = new SentenceVo();
		vo.setId(s.getId());
		vo.setContent(s.getContent());
		vo.setLabels(s.getLabels());
		vo.setLike(s.getLike());
		vo.setDislike(s.getDislike());

		if (like != null) {
			vo.setLikeByUser(Integer.valueOf(1).equals(like.getLike()));
			vo.setDislikeByUser(Integer.valueOf(1).equals(like.getDislike()));
		}

		return vo;
	}
}
