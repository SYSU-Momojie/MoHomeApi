package com.cn.momojie.basic.vo;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageResult<T> {

    private Long total;

    private List<T> list;

    public static <T> PageResult fromList(List<T> l) {
        PageInfo<T> info = new PageInfo<>(l);
        PageResult<T> result = new PageResult<>();
        result.setTotal(info.getTotal());
        result.setList(info.getList());
        return result;
    }

    public static <T> PageResult empty() {
        PageResult<T> result = new PageResult<>();
        result.setTotal(0L);
        result.setList(new ArrayList<>());
        return result;
    }
}
