package com.cn.momojie.blog.dao;

import com.cn.momojie.blog.param.ArticleParam;
import com.cn.momojie.blog.vo.BlogArticle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogArticleDao {

    void add(BlogArticle art);

    void updateById(BlogArticle art);

    Long getMaxId(@Param("user") String user);

    List<BlogArticle> selectByParam(ArticleParam param);
}
