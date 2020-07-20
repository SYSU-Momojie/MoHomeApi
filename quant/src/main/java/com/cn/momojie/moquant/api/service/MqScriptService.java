package com.cn.momojie.moquant.api.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cn.momojie.moquant.api.config.MqPythonConfig;
import com.cn.momojie.redis.client.RedisClient;
import com.cn.momojie.redis.config.LettuceConfig;
import com.cn.momojie.utils.DateTimeUtils;
import com.cn.momojie.utils.ThreadPoolUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MqScriptService {

	@Autowired
	private RedisClient redisClient;

	@Autowired
	@Qualifier("mqPythonConfig")
	private MqPythonConfig pythonConfig;

	public int dailyUpdate() {
		Boolean flag = redisClient.setNX("fetch_daily", "1", 3600 * 4);
		if (!flag) {
			log.error("fetch_daily获取锁失败");
			return 0;
		}
		String dt = DateTimeUtils.getTodayDt();
		String args = String.format("--job fetch_daily --date %s", dt);
		int r = commonExecute(args);
		redisClient.del("fetch_daily");
		return r;
	}

	public void recalculateFrom(Map<String, String> codeDateMap) {
		List<Future> resultList = new ArrayList<>();
		for (String tsCode: codeDateMap.keySet()) {
			String dt = codeDateMap.get(tsCode);
			resultList.add(ThreadPoolUtils.getPythonPool().submit(() -> recalculateFrom(tsCode, dt)));
		}
		for (Future f: resultList) {
			try {
				f.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	protected int recalculateFrom(String tsCode, String fromDate) {
		String args = String.format("--job recalculate --code %s --from-date %s", tsCode, fromDate);
		return commonExecute(args);
	}

	private int commonExecute(String args) {
		String python = pythonConfig.getPyBin();
		String scriptPath = pythonConfig.getMqScriptDir();
		File runPythonFile = new File(scriptPath, "run.py");
		String command = String.format("%s %s %s", python, runPythonFile.getAbsolutePath(), args);
		log.info("执行脚本命令 {}", command);
		try {
			Process proc = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			// 打印python执行结果
			String line = null;
			while ((line = in.readLine()) != null) {
				log.info(line);
			}
			in.close();
			int r = proc.waitFor();
			log.info("执行脚本结果 {}", r);
			return r;
		} catch (Exception e) {
			log.error("执行脚本失败", e);
		}
		return 1;
	}
}
