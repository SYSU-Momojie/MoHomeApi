package com.cn.momojie.blog.controller;

import com.cn.momojie.blog.SessionUtil;
import com.cn.momojie.blog.constant.MsgCode;
import com.cn.momojie.blog.dao.BlogArticleDao;
import com.cn.momojie.blog.param.ArticleParam;
import com.cn.momojie.blog.vo.BlogArticle;
import com.cn.momojie.blog.vo.MessageResult;
import com.cn.momojie.blog.vo.PageModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("article")
@Controller
public class ArticleController {

    @Autowired
    private BlogArticleDao articleDao;

    @RequestMapping(path = "post", method = RequestMethod.POST)
    @ResponseBody
    public MessageResult<Long> postArticle(@RequestBody BlogArticle art) {
        MessageResult<Long> result = new MessageResult<>();
        String user = SessionUtil.getUserName();
        art.setCreatedBy(user);
        Long callbackId = null;
        if (art.getId() == null) {
            articleDao.add(art);
            callbackId = art.getId();
        } else {
            articleDao.updateById(art);
            callbackId = art.getId();
        }
        result.setData(callbackId);
        return result;
    }

    @RequestMapping(path = "getPage", method = RequestMethod.GET)
    @ResponseBody
    public MessageResult<PageModel<BlogArticle>> getPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String type) {
        MessageResult<PageModel<BlogArticle>> result = new MessageResult<>();
        PageHelper.startPage(pageNum, pageSize);
        ArticleParam param = new ArticleParam();
        param.setType(type);
        List<BlogArticle> articles = articleDao.selectByParam(param);
        PageInfo<BlogArticle> pageInfo = new PageInfo<>(articles);

        PageModel<BlogArticle> pageModel = new PageModel<>();
        pageModel.setTotal(pageInfo.getTotal());
        pageModel.setList(pageInfo.getList());
        result.setData(pageModel);
        return result;
    }

    @RequestMapping(path = "getArticle", method = RequestMethod.GET)
    @ResponseBody
    public MessageResult<BlogArticle> getArticle(@RequestParam Long id) {
        MessageResult<BlogArticle> result = new MessageResult<>();
        ArticleParam param = new ArticleParam();
        param.setId(id);
        BlogArticle art = articleDao.selectByParam(param).stream().findFirst().orElse(null);
        result.setCode(art == null ? MsgCode.FAIL : MsgCode.SUCCESS);
        result.setData(art);
        return result;
    }
}
