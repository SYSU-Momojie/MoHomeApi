package com.cn.momojie.moquant.api.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.momojie.moquant.api.config.MqPythonConfig;
import com.cn.momojie.utils.ThreadPoolUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MqScriptService {

	@Autowired
	private MqPythonConfig pythonConfig;

	public void recalculateFrom(Map<String, String> codeDateMap) {
		String scriptPath = pythonConfig.getMqScriptDir();
		for (String tsCode: codeDateMap.keySet()) {
			String dt = codeDateMap.get(tsCode);
			ThreadPoolUtils.getPythonPool().execute(() -> recalculateFrom(scriptPath, tsCode, dt));
		}
	}

	protected void recalculateFrom(String scriptPath, String tsCode, String fromDate) {
		File runPythonFile = new File(scriptPath, "run.py");
		String command = String.format("python %s --job recalculate --code %s --from-date %s",
				runPythonFile.getAbsolutePath(), tsCode, fromDate);
		commonExecute(command);
	}

	private void commonExecute(String command) {
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
		} catch (Exception e) {
			log.error("执行脚本失败", e);
		}
	}
}
