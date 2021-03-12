package com.cn.momojie.es;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.momojie.es.po.MoTalkLabel;
import com.cn.momojie.es.repo.MoTalkLabelRepo;

public class MoTalkLabelTeset extends BaseTest {

	@Autowired
	private MoTalkLabelRepo lr;

	@Test
	public void removeAll() {
		lr.deleteAll();
	}

	@Test
	public void add() {
		removeAll();

		String[] all = {"全部", "搞笑", "情话", "怼人", "装傻", "其他"};
		List<MoTalkLabel> labels = Arrays.stream(all).map(i -> {
			MoTalkLabel l1 = new MoTalkLabel();
			l1.setLabel(i);
			return l1;
		}).collect(Collectors.toList());

		lr.saveAll(labels);
	}
}
