package com.cn.momojie.moquant.api.service;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cn.momojie.utils.ThreadPoolUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MqScheduleService {

	@Autowired
	private MqScriptService scriptService;

	@Autowired
	private MqSysParamService paramService;

	@Scheduled(cron = "10 * * * * *")
	public void heartBeat() {
		log.info("Schedule service is alive");
	}

	@Scheduled(cron = "0 59 15,17,19,21,23 * * *")
	public void dailyUpdate() {
		if (!isOn("daily_update")) {
			log.info("跳过 daily_update");
			return ;
		}
		Future<Integer> f = ThreadPoolUtils.getPythonPool().submit( () -> scriptService.dailyUpdate());
		try {
			f.get(4, TimeUnit.HOURS);
		} catch (Exception e) {
			log.error("定时任务 dailyUpdate 出错", e);
		}
	}

	private Boolean isOn(String job) {
		String key = String.format("SCHEDULE_%s", job.toUpperCase());
		return "1".equals(paramService.getString(key));
	}
}
