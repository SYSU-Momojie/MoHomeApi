package com.cn.momojie.moquant.api.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.momojie.moquant.api.constant.MqIndicatorEnum;
import com.cn.momojie.moquant.api.constant.MqReportType;
import com.cn.momojie.moquant.api.dao.MqManualIndicatorDao;
import com.cn.momojie.moquant.api.dao.MqShareNoteDao;
import com.cn.momojie.moquant.api.dao.MqShareNoteRelationDao;
import com.cn.momojie.moquant.api.dto.MqManualIndicator;
import com.cn.momojie.moquant.api.dto.MqShareNote;
import com.cn.momojie.moquant.api.dto.MqShareNoteRelation;
import com.cn.momojie.moquant.api.param.manage.NoteEditParam;
import com.cn.momojie.moquant.api.vo.OperationResp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MqManageService {

	@Autowired
	private MqShareNoteDao noteDao;

	@Autowired
	private MqShareNoteRelationDao noteRelationDao;

	@Autowired
	private MqManualIndicatorDao manualIndicatorDao;

	/**
	 * 编辑日志
	 *
	 * @param input 日志主体
	 * @param add 是否新增
	 * @return
	 */
	public OperationResp editNote(NoteEditParam input, Boolean add) {
		if (StringUtils.isEmpty(input.getEventBrief())) {
			return OperationResp.fail("简述不能为空", null);
		}
		if (!add && (input.getId() == null || Long.valueOf(0).equals(input.getId()))) {
			return OperationResp.fail("编辑ID为空", null);
		}

		MqShareNote note = new MqShareNote();
		note.setId(input.getId());
		note.setEventBrief(input.getEventBrief());
		note.setNoteDetail(input.getNoteDetail());
		note.setNoteConclusion(input.getNoteConclusion());

		try {
			if (add) {
				noteDao.insert(note);
			} else {
				noteDao.updateById(note);
			}

			Map<String, Object> queryMap = new HashMap<>();
			queryMap.put("note_id", note.getId());
			noteRelationDao.deleteByMap(queryMap);

			for (String tsCode : input.getTsCodeList()) {
				MqShareNoteRelation r = new MqShareNoteRelation();
				r.setTsCode(tsCode);
				r.setNoteId(note.getId());
				noteRelationDao.insert(r);
			}

		} catch (Exception e) {
			log.error("编辑插入数据库失败", e);
			return OperationResp.fail("编辑插入数据库失败", null);
		}

		return OperationResp.ok("保存日记成功", note.getId());
	}

	public OperationResp deleteNote(Long noteId) {
		try {
			noteDao.deleteById(noteId);
			Map<String, Object> queryMap = new HashMap<>();
			queryMap.put("note_id", noteId);
			noteRelationDao.deleteByMap(queryMap);
		} catch (Exception e) {
			log.error("删除数据失败", e);
			return OperationResp.fail("删除数据失败", null);
		}
		return OperationResp.ok("删除数据成功", null);
	}

	public OperationResp<Map<String, String>> manualInput(List<MqManualIndicator> inputList) {
		String err = fieldsCheck(inputList);
		if (err.length() > 0) {
			return OperationResp.fail(err, null);
		}

		StringBuilder sb = new StringBuilder();
		int rowNum = 2;
		Map<String, String> codeDateMap = new HashMap<>();
		for (MqManualIndicator i: inputList) {
			try {
				manualIndicatorDao.insert(i);
				String code = i.getTsCode();
				String date = i.getUpdateDate();
				if (!codeDateMap.containsKey(code)) {
					codeDateMap.put(code, date);
				} else if (date.compareTo(codeDateMap.get(code)) < 0){
					codeDateMap.put(code, date);
				}
			} catch (Exception e) {
				log.error("插入数据库失败", e);
				sb.append("行").append(rowNum).append("插入失败;\n");
			}

			rowNum += 1;
		}

		if (codeDateMap.size() > 0) {
			return OperationResp.ok("导入成功", codeDateMap);
		} else {
			return OperationResp.fail("导入失败", null);
		}
	}

	private String fieldsCheck(List<MqManualIndicator> inputList) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat updateDate = new SimpleDateFormat("yyyyMMdd");

		int i = 2;
		for (MqManualIndicator mi: inputList) {
			StringBuilder row = new StringBuilder();
			if (StringUtils.isEmpty(mi.getTsCode())) {
				row.append("TS代码为空;");
			}

			if (StringUtils.isEmpty(mi.getUpdateDate())) {
				row.append("更新日期为空;");
			} else {
				try {
					updateDate.parse(mi.getUpdateDate());
				} catch (ParseException e) {
					row.append("不合法的更新日期;");
				}
			}

			if (StringUtils.isEmpty(mi.getPeriod())) {
				row.append("报告期为空;");
			} else if (mi.getPeriod().length() != 6 && mi.getPeriod().length() != 8) {
				row.append("不合法的报告期;");
			} else if (mi.getPeriod().length() == 6) {
				if (mi.getUpdateDate().endsWith("Q1")) {
					mi.setUpdateDate(mi.getUpdateDate().substring(0, 4) + "0331");
				} else if(mi.getUpdateDate().endsWith("Q2")) {
					mi.setUpdateDate(mi.getUpdateDate().substring(0, 4) + "0630");
				} else if (mi.getUpdateDate().endsWith("Q3")) {
					mi.setUpdateDate(mi.getUpdateDate().substring(0, 4) + "0930");
				} else if (mi.getUpdateDate().endsWith("Q4")) {
					mi.setUpdateDate(mi.getUpdateDate().substring(0, 4) + "1231");
				}
				try {
					updateDate.parse(mi.getUpdateDate());
				} catch (ParseException e) {
					row.append("不合法的报告期;");
				}
			} else {
				try {
					updateDate.parse(mi.getUpdateDate());
					if (!(mi.getUpdateDate().endsWith("0331") || mi.getUpdateDate().endsWith("0630") ||
							mi.getUpdateDate().endsWith("0930") || mi.getUpdateDate().endsWith("1231")
					)) {
						row.append("不合法的报告期;");
					}
				} catch (ParseException e) {
					row.append("不合法的报告期;");
				}
			}

			if (!MqReportType.valid(mi.getReportType())) {
				row.append("不合法的类型;");
			}

			if (!MqIndicatorEnum.valid(mi.getName())) {
				row.append("不合法的指标;");
			}

			if (mi.getValue() == null) {
				row.append("值为空");
			}

			if (row.length() > 0) {
				sb.append("行").append(i).append(':').append(row).append('\n');
			}

			i += 1;
		}

		return sb.toString();
	}
}
