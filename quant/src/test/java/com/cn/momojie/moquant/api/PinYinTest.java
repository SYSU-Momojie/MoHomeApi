package com.cn.momojie.moquant.api;

import org.junit.Assert;
import org.junit.Test;

import com.cn.momojie.utils.PinYinUtils;

public class PinYinTest {

	@Test
	public void convert() {
		String str = "ST中葡";
		String py = PinYinUtils.convertToPyFirst(str);
		System.out.println(py);
		Assert.assertTrue(py.contains("ST"));
	}
}
