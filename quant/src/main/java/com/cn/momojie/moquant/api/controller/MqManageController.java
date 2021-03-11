package com.cn.momojie.moquant.api.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cn.momojie.moquant.api.constant.MqExcelConst;
import com.cn.momojie.moquant.api.dto.MqManualIndicator;
import com.cn.momojie.moquant.api.param.manage.NoteEditParam;
import com.cn.momojie.moquant.api.service.MqManageService;
import com.cn.momojie.moquant.api.service.MqScriptService;
import com.cn.momojie.basic.vo.OperationResp;
import com.cn.momojie.utils.ExcelUtils;
import com.cn.momojie.utils.ThreadPoolUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/manage")
public class MqManageController {

	@Autowired
	private MqManageService manageService;

	@Autowired
	private MqScriptService scriptService;

	@RequestMapping(path = "/addNote", method = RequestMethod.POST)
	public OperationResp addNote(@RequestBody NoteEditParam input) {
		return manageService.editNote(input, true);
	}

	@RequestMapping(path = "/editNote", method = RequestMethod.POST)
	public OperationResp editNote(@RequestBody NoteEditParam input) {
		return manageService.editNote(input, false);
	}

	@RequestMapping(path = "/deleteNote", method = RequestMethod.POST)
	public OperationResp deleteNote(@RequestBody Long noteId) {
		return manageService.deleteNote(noteId);
	}

	@RequestMapping(path = "uploadManual", method = RequestMethod.POST)
	public OperationResp uploadManual(MultipartFile file) {
		try (InputStream is = file.getInputStream()) {
			List<MqManualIndicator> inputList = ExcelUtils.getModel(is, MqManualIndicator.class, MqExcelConst.MANUAL_FIELDS);
			OperationResp<Map<String, String>> importResult = manageService.manualInput(inputList);

			if (importResult.getSuccess()) {
				ThreadPoolUtils.getServicePool().submit(() -> {
					Map<String, String> codeDateMap = importResult.getData();
					scriptService.recalculateFrom(codeDateMap);
					log.info("人工录入后重算成功 个数: {}", codeDateMap.size());
				});
			}

			return importResult;
		} catch (Exception e) {
			log.error("上传文件解析失败", e);
			return OperationResp.fail("上传文件解析失败", null);
		}
	}
}
