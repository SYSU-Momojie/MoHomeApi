package com.cn.momojie.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtils {

	private volatile static ExecutorService pythonPool;

	private volatile static ExecutorService servicePool;

	public static ExecutorService getPythonPool() {
		if (pythonPool != null) {
			return pythonPool;
		}
		synchronized (ThreadPoolUtils.class) {
			if (pythonPool != null) {
				return pythonPool;
			}
			pythonPool = Executors.newFixedThreadPool(16);
		}
		return pythonPool;
	}

	public static ExecutorService getServicePool() {
		if (servicePool != null) {
			return servicePool;
		}
		synchronized (ThreadPoolUtils.class) {
			if (servicePool != null) {
				return servicePool;
			}
			servicePool = Executors.newFixedThreadPool(16);
		}
		return servicePool;
	}
}
