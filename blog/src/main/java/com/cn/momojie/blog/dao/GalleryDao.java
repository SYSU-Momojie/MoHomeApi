package com.cn.momojie.blog.dao;

import com.cn.momojie.blog.vo.GalleryGroupVo;
import com.cn.momojie.blog.vo.GalleryPicVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GalleryDao {

    List<GalleryGroupVo> getGroups();

    GalleryGroupVo getGroupByCode(@Param("code") String code);

    void addGalleryPic(GalleryPicVo vo);
}
