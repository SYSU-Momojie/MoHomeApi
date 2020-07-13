package com.cn.momojie.blog.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

    List<String> getRoles(@Param("name") String name);

    List<String> getAuths(@Param("name") String name);
}
