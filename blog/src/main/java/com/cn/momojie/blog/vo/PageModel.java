package com.cn.momojie.blog.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageModel<T> {

    private List<T> list;

    private Long total;

}
