package com.cn.momojie.moquant.api.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.cn.momojie.basic.vo.PageResult;
import com.cn.momojie.moquant.api.constant.MqMetricEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cn.momojie.moquant.api.constant.MqReportType;
import com.cn.momojie.moquant.api.dao.*;
import com.cn.momojie.moquant.api.dto.*;
import com.cn.momojie.moquant.api.dto.ts.TsBasic;
import com.cn.momojie.moquant.api.dto.ts.TsForecast;
import com.cn.momojie.moquant.api.param.*;
import com.cn.momojie.utils.DateTimeUtils;
import com.cn.momojie.utils.PinYinUtils;
import com.cn.momojie.moquant.api.vo.*;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MqInfoQueryService {

	@Autowired
	private TsBasicDao tsBasicDao;

	@Autowired
	private TsForecastDao tsForecastDao;

    @Autowired
    private MqShareNoteDao noteDao;

    @Autowired
    private MqMessageDao messageDao;

    @Autowired
	private MqMetricService metricService;

    public MqShareAll getLatestByCode(String tsCode) {
		List<String> codeList = Arrays.asList(tsCode);

		Map<String, TsBasic> basicMap = metricService.getBasicMap(codeList);
		Map<String, Map<String, MqDailyMetric>> dailyMap = metricService.getLatestDailyMetric(codeList, null);
		Map<String, Map<String, MqQuarterMetric>> quarterMap = metricService.getLatestQuarterMetric(codeList, null);

		MqShareAll all = new MqShareAll();
		all.setTsCode(tsCode);

		TsBasic basic = basicMap.get(tsCode);
		if (basic == null) {
			log.error("Can't find ts_basic of {}", tsCode);
		} else {
			all.setShareName(basic.getName());
		}

		all.setDailyMetric(dailyMap.getOrDefault(tsCode, new HashMap<>()));
		all.setQuarterMetric(quarterMap.getOrDefault(tsCode, new HashMap<>()));

    	return all;
    }

	public MqShareTrend getTrend(MqTrendParam input) {
		MqShareTrend trend = new MqShareTrend();

		MqMetricEnum i = MqMetricEnum.fromName(input.getMetricName());
		if (i == null) {
			log.error("找不到对应的指标");
			return trend;
		}

		if (i.isDaily) {
			List<MqDailyMetric> list = metricService.getDailyOrderByDateAsc(input.getTsCode(), i.name);
			for (MqDailyMetric mdi: list) {
				addToTrend(trend, mdi.getUpdateDate(), mdi.getValue(), null);
			}
		} else {
			List<MqQuarterMetric> list = metricService.getQuarterByPeriodAsc(input.getTsCode(), i.name);
			for (MqQuarterMetric mqi: list) {
				addToTrend(trend, DateTimeUtils.convertToQuarter(mqi.getPeriod()), mqi.getValue(), mqi.getYoy());
			}
		}

    	return trend;
	}

	private Boolean isQ4(String period) {
    	return period != null && (period.endsWith("1231") || period.endsWith("Q4"));
	}

	private void addToTrend(MqShareTrend trend, String x, BigDecimal y1, BigDecimal y2) {
    	if (trend == null || x == null) {
    		return ;
		}
    	if (y1 == null && y2 == null) {
    		return ;
		}
    	if (x.length() == 8) {
    		x = x.substring(0, 4) + '-' + x.substring(4, 6) + '-' + x.substring(6, 8);
		}
    	trend.getX().add(x);
    	trend.getVl1().add(y1);
    	trend.getVl2().add(y2);
	}

	public List<ShareListItem> getAllShare() {
		List<TsBasic> list = tsBasicDao.getAllForSearchList();
		return list.stream().map(i -> {
			ShareListItem ret = new ShareListItem();
			ret.setTsCode(i.getTsCode());
			ret.setName(i.getName());
			ret.setPy(PinYinUtils.convertToPyFirst(i.getName()));
			return ret;
		}).collect(Collectors.toList());
	}

	public PageResult<MqShareNoteVo> getNotes(CodePageParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		if ("all".equals(param.getTsCode())) {
			param.setTsCode(null);
		}
		List<MqShareNoteVo> noteList = noteDao.getByCode(param.getTsCode());
		List<Long> noteIdList = noteList.stream().map(MqShareNoteVo::getId).collect(Collectors.toList());
		List<MqShareNoteRelationVo> relationList = CollectionUtils.isEmpty(noteIdList) ? new ArrayList<>() : noteDao.getRelated(noteIdList);
		Map<Long, List<MqShareNoteRelationVo>> relationMap = relationList.stream().collect(Collectors.groupingBy(MqShareNoteRelationVo::getNoteId));
		for (MqShareNoteVo note: noteList) {
			note.setRelatedShareList(relationMap.get(note.getId()));
			if (note.getRelatedShareList() == null) {
				note.setRelatedShareList(new ArrayList<>());
			}
		}
		return PageResult.fromList(noteList);
	}

	public MqShareNoteVo getNote(Long noteId) {
    	MqShareNote note = noteDao.selectById(noteId);
    	if (note == null) {
    		return null;
		}
    	MqShareNoteVo vo = new MqShareNoteVo();
		BeanUtils.copyProperties(note, vo);
    	List<MqShareNoteRelationVo> relationList = noteDao.getRelated(Arrays.asList(noteId));
    	vo.setRelatedShareList(relationList);
    	return vo;
	}

	public MqForecastInfo getForecastInfo(String code) {
		MqForecastInfo info = new MqForecastInfo();

		Map<String, Map<String, MqQuarterMetric>> codeNameMap = metricService.getLatestQuarterMetric(Arrays.asList(code), Arrays.asList("nprofit", "dprofit"));
		Map<String, MqQuarterMetric> nameMap = codeNameMap.get(code);
		if (nameMap == null) {
			return info;
		}

		MqQuarterMetric nprofit = nameMap.get("nprofit");
		if (nprofit == null) {
			return info;
		}

		String period = nprofit.getPeriod();
		Integer reportType = nprofit.getReportType();

		if (reportType == null || !(
				(reportType & (1 << MqReportType.EXPRESS)) > 0 || (reportType & (1 << MqReportType.FORECAST)) > 0
		)) {
			return info;
		}

		info.setPeriod(period);

		TsForecast forecast = tsForecastDao.selectOne(code, period);
		if (forecast != null && forecast.getChangeReason() != null) {
			info.setLatest(true);
			info.setForecastReason(forecast.getChangeReason());
		}


		if (nprofit != null && nprofit.getValue() != null) {
			info.setNprofit(nprofit.getValue());
		}

		MqQuarterMetric dprofit = nameMap.get("dprofit");
		if (dprofit != null && dprofit.getValue() != null) {
			info.setDprofit(dprofit.getValue());
		}

		return info;
	}

	public PageResult<MqMessage> getLatestReportList(MessageParam param) {
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		return PageResult.fromList(messageDao.getLatestByType(param.getMsgType()));
	}
}
