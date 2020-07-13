package com.cn.momojie.moquant.api.util;

import org.junit.Assert;
import org.junit.Test;

import com.cn.momojie.utils.DateTimeUtils;

public class DateTimeUtilsTest {

	@Test
	public void testDt() {
		Assert.assertTrue(DateTimeUtils.getTodayDt().compareTo(DateTimeUtils.getYesterdayDt()) > 0);
	}
}
