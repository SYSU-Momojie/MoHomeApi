package com.cn.momojie.auth.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.cn.momojie.auth.dto.UserDto;
import com.cn.momojie.auth.param.UserParam;

public interface UserDao {

	void create(UserDto user);

	UserDto getUser(UserParam param);

    List<String> getRoles(@Param("name") String name);
}
