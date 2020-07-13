package com.cn.momojie.moquant.api.service;

import java.io.File;
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
		for (String tsCode: codeDateMap.keySet()) {
			String dt = codeDateMap.get(tsCode);
			ThreadPoolUtils.getPythonPool().execute(() -> recalculateFrom(tsCode, dt));
		}
	}

	private void recalculateFrom(String tsCode, String fromDate) {
		String scriptPath = pythonConfig.getMqScriptDir();
		File runPythonFile = new File(scriptPath, "run.py");
		String command = String.format("python %s --job recalculate --code %s --from-date %s",
				runPythonFile.getAbsolutePath(), tsCode, fromDate);
		commonExecute(command);
	}

	private void commonExecute(String command) {
		try {
			Process proc = Runtime.getRuntime().exec(command);
			proc.waitFor();
		} catch (Exception e) {
			log.error("执行脚本失败", e);
		}
	}
}
