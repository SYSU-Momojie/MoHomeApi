package com.cn.momojie.talk.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.momojie.talk.dto.UserLike;

public interface UserLikeDao {

	List<UserLike> getSpecify(@Param("sentence_type") String sentenceType, @Param("user_id") Long userId, @Param("sentence_ids") List<String> sentenceIds);
}
