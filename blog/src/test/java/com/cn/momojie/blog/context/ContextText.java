package com.cn.momojie.blog.context;

import com.cn.momojie.blog.BaseTest;
import com.cn.momojie.blog.session.SessionOpService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ContextText extends BaseTest {

	@Autowired
	private SessionOpService bean;

	@Test
	public void contextLoaded() {
		Assert.assertNotNull(bean);
	}
}
