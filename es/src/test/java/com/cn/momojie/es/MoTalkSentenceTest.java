package com.cn.momojie.es;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.cn.momojie.es.po.MoTalkSentence;
import com.cn.momojie.es.repo.MoTalkSentenceRepo;
import com.cn.momojie.utils.DateTimeUtils;

public class MoTalkSentenceTest extends BaseTest {

	@Autowired
	private MoTalkSentenceRepo gsr;

	@Test
	public void testAdd() {
		MoTalkSentence ss = new MoTalkSentence();
		ss.setContent("这只是一个非常简单简单的句子");
		ss.setLabels(Arrays.asList("测试"));
		ss.setLike(1);

		gsr.save(ss);

		MoTalkSentence ss2 = new MoTalkSentence();
		ss2.setContent("这只是一个简单的句子");
		ss2.setLabels(Arrays.asList("测试"));
		ss2.setLike(1);

		gsr.save(ss2);

		MoTalkSentence ss3 = new MoTalkSentence();
		ss3.setContent("这只是一个简单的句子");
		ss3.setLabels(Arrays.asList("测试"));
		ss3.setLike(2);

		gsr.save(ss3);
	}

	@Test
	public void searchAndDelete() {
		Page<MoTalkSentence> ssl = gsr.findByLabelsRandomly("测试", String.valueOf(DateTimeUtils.getCurrentTimestamp()), PageRequest.of(0, 20));
		for (MoTalkSentence ss: ssl) {
			gsr.delete(ss);
		}
	}

	@Test
	public void searchByContentAndLabels() {
		searchAndDelete();
		testAdd();
		Page<MoTalkSentence> gsp = gsr.findByContentAndLabels("简单","测试", PageRequest.of(0, 5));
		Assert.assertEquals(3, gsp.getNumberOfElements());
		List<MoTalkSentence> ssl = gsp.getContent();
		Assert.assertEquals(Integer.valueOf(2), ssl.get(0).getLike());
		Assert.assertEquals("这只是一个非常简单简单的句子", ssl.get(1).getContent());
		searchAndDelete();
	}

	@Test
	public void searchByPage() {
		searchAndDelete();
		testAdd();

		Page<MoTalkSentence> gsp = gsr.findByContentAndLabels("简单","测试", PageRequest.of(0, 1));
		Assert.assertEquals(1, gsp.getNumberOfElements());
		List<MoTalkSentence> ssl = gsp.getContent();
		Assert.assertEquals(Integer.valueOf(2), ssl.get(0).getLike());

		gsp = gsr.findByContentAndLabels("简单","测试", PageRequest.of(1, 1));
		Assert.assertEquals(1, gsp.getNumberOfElements());
		ssl = gsp.getContent();
		Assert.assertEquals("这只是一个非常简单简单的句子", ssl.get(0).getContent());
		searchAndDelete();
	}
}
