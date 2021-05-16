package com.cn.momojie.moquant.api.controller;

import com.cn.momojie.basic.vo.PageResult;
import com.cn.momojie.moquant.api.dto.MqShareAll;
import com.cn.momojie.moquant.api.param.CodePageParam;
import com.cn.momojie.moquant.api.param.DailyBasicParam;
import com.cn.momojie.moquant.api.param.MessageParam;
import com.cn.momojie.moquant.api.param.MqShareListParam;
import com.cn.momojie.moquant.api.param.MqTrendParam;
import com.cn.momojie.moquant.api.service.MqInfoQueryService;
import com.cn.momojie.moquant.api.vo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/quant/stock")
public class MqStockController {

    @Autowired
    private MqInfoQueryService mqInfoQueryService;

    @RequestMapping(path = "getLatestByCode", method = RequestMethod.POST)
    public MqShareAll getLatestByCode(@RequestBody String tsCode) {
        return mqInfoQueryService.getLatestByCode(tsCode);
    }

    @RequestMapping(path = "getTrendByCode", method = RequestMethod.POST)
    public MqShareTrend getTrendByCode(@RequestBody MqTrendParam param) {
        return mqInfoQueryService.getTrend(param);
    }

    @RequestMapping(path = "getAllShareForSearch", method = RequestMethod.POST)
    public List<ShareListItem> getAllShareForSearch() {
        return mqInfoQueryService.getAllShare();
    }

    @RequestMapping(path = "getNoteList", method = RequestMethod.POST)
    public PageResult getNoteList(@RequestBody CodePageParam param) {
        return mqInfoQueryService.getNotes(param);
    }

	@RequestMapping(path = "getNote", method = RequestMethod.POST)
    public MqShareNoteVo getNote(@RequestBody Long id) {
    	return mqInfoQueryService.getNote(id);
	}

	@RequestMapping(path = "getForecastInfo", method = RequestMethod.POST)
	public MqForecastInfo getForecastInfo(@RequestBody String tsCode) {
		return mqInfoQueryService.getForecastInfo(tsCode);
	}

	@RequestMapping(path = "getLatestReportList", method = RequestMethod.POST)
	public PageResult getLatestReportList(@RequestBody MessageParam param) {
    	return mqInfoQueryService.getLatestReportList(param);
	}
}
