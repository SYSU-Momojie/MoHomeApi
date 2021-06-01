package com.cn.momojie.moquant.api.service;

import com.cn.momojie.basic.vo.PageResult;
import com.cn.momojie.moquant.api.dao.MqSharePoolDao;
import com.cn.momojie.moquant.api.dto.MqDailyMetric;
import com.cn.momojie.moquant.api.dto.MqQuarterMetric;
import com.cn.momojie.moquant.api.dto.MqShareAll;
import com.cn.momojie.moquant.api.dto.MqSharePool;
import com.cn.momojie.moquant.api.dto.ts.TsBasic;
import com.cn.momojie.moquant.api.param.strategy.MqSharePoolParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MqSharePoolService {

    @Autowired
    private MqSharePoolDao poolDao;

    @Autowired
    private MqMetricService metricService;

    public PageResult<MqShareAll> getGrowList(MqSharePoolParam param) {
        return getStrategyList("grow_score", Arrays.asList("pe"), Arrays.asList("revenue_quarter", "dprofit_quarter"));
    }

    public PageResult<MqShareAll> getValList(MqSharePoolParam param) {
        return getStrategyList("dividend_score", Arrays.asList("pe", "pb", "dividend_yields"), Arrays.asList(""));
    }

    public PageResult<MqShareAll> getDownGapList(MqSharePoolParam param) {
        return getStrategyList("down_gap", Arrays.asList("mv", "pe", "pb"), Arrays.asList(""));
    }

    public PageResult<MqShareAll> getStrategyList(String strategy,
                                                  Collection<String> dailyMetric, Collection<String> quarterMetric) {
        List<MqSharePool> shareList = poolDao.getLatestByStrategy(strategy);

        if (shareList.size() == 0) {
            return PageResult.fromList(new ArrayList<>());
        }

        List<String> codeList = shareList.stream().map(MqSharePool::getTsCode).collect(Collectors.toList());
        String strategyDt = shareList.get(0).getDt();

        PageResult<MqShareAll> result = new PageResult<>();
        result.setTotal(Long.valueOf(codeList.size()));

        Map<String, TsBasic> basicMap = metricService.getBasicMap(codeList);
        Map<String, Map<String, MqDailyMetric>> dailyMap = metricService.getDailyMetric(codeList, dailyMetric, strategyDt);
        Map<String, Map<String, MqQuarterMetric>> quarterMap = metricService.getLatestQuarterMetric(codeList, quarterMetric, strategyDt);

        List<MqShareAll> pageList = new ArrayList<>(codeList.size());

        for (String tsCode: codeList) {
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
            pageList.add(all);
        }
        result.setList(pageList);

        return result;
    }
}
