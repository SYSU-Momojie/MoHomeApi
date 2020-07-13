package com.cn.momojie.blog.vo;

import lombok.Data;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class BlogArticle {

    private Long id;

    private String type;

    private String title;

    private String content;

    private Date createdTime;

    private String createdBy;

    private Date updatedTime;

    private String updatedBy;
}
