package com.cn.momojie.es;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.momojie.es.po.MoTalkLabel;
import com.cn.momojie.es.repo.MoTalkLabelRepo;

public class MoTalkLabelTeset extends BaseTest {

	@Autowired
	private MoTalkLabelRepo lr;

	@Test
	public void add() {
		MoTalkLabel l1 = new MoTalkLabel();
		l1.setLabel("全部");

		MoTalkLabel l2 = new MoTalkLabel();
		l2.setLabel("搞笑");

		MoTalkLabel l3 = new MoTalkLabel();
		l3.setLabel("情话");

		MoTalkLabel l4 = new MoTalkLabel();
		l4.setLabel("其他");

		lr.saveAll(Arrays.asList(l1, l2, l3, l4));
	}
}
