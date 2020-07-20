package com.cn.momojie.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;

public class ThreadPoolUtilsTest {

	@Test
	public void testTimeout() {
		Future<Integer> f = ThreadPoolUtils.getPythonPool().submit(this::fakeMethod);
		try {
			f.get(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (ExecutionException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}

	private int fakeMethod() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 1;
	}
}
