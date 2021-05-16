package com.cn.momojie.moquant.api.service;

import com.cn.momojie.moquant.api.dao.MqDailyMetricDao;
import com.cn.momojie.moquant.api.dao.MqQuarterMetricDao;
import com.cn.momojie.moquant.api.dao.TsBasicDao;
import com.cn.momojie.moquant.api.dto.MqDailyMetric;
import com.cn.momojie.moquant.api.dto.MqQuarterMetric;
import com.cn.momojie.moquant.api.dto.ts.TsBasic;
import com.cn.momojie.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MqMetricService {

    @Autowired
    private MqDailyMetricDao dailyMetricDao;

    @Autowired
    private MqQuarterMetricDao quarterMetricDao;

    @Autowired
    private TsBasicDao tsBasicDao;


    public Map<String, TsBasic> getBasicMap(Collection<String> codeList) {
        Map<String, TsBasic> result = new HashMap<>();
        if (CollectionUtils.isEmpty(codeList)) {
            return result;
        }

        List<TsBasic> basicList = tsBasicDao.selectByCodes(codeList);
        for (TsBasic basic: basicList) {
            result.put(basic.getTsCode(), basic);
        }

        return result;
    }

    /**
     * 获取最新的天指标
     * @param codeList 股票编码列表
     * @param nameList 股票编码列表
     * @return
     */
    public Map<String, Map<String, MqDailyMetric>> getLatestDailyMetric(Collection<String> codeList, Collection<String> nameList) {
        return getDailyMetric(codeList, nameList, DateTimeUtils.getTodayDt());
    }

    /**
     * 获取天指标
     * @param codeList 股票编码列表
     * @param nameList 股票编码列表
     * @param underDate 截止哪天最新的指标
     * @return
     */
    public Map<String, Map<String, MqDailyMetric>> getDailyMetric(Collection<String> codeList, Collection<String> nameList, String underDate) {
        Map<String, Map<String, MqDailyMetric>> codeNameMap = new HashMap<>();
        if (CollectionUtils.isEmpty(codeList)) {
            return codeNameMap;
        }

        List<MqDailyMetric> metricList = dailyMetricDao.getDailyLatest(codeList, nameList, underDate);
        for (MqDailyMetric i: metricList) {
            String tsCode = i.getTsCode();
            String name = i.getName();
            if (!codeNameMap.containsKey(tsCode)) {
                codeNameMap.put(tsCode, new HashMap<>());
            }
            Map<String, MqDailyMetric> nameMap = codeNameMap.get(tsCode);
            nameMap.put(name, i);
        }

        return codeNameMap;
    }


    /**
     * 获取最新季度指标
     * @param codeList 股票编码列表
     * @param nameList 股票编码列表
     * @return
     */
    public Map<String, Map<String, MqQuarterMetric>> getLatestQuarterMetric(Collection<String> codeList, Collection<String> nameList) {
        return getLatestQuarterMetric(codeList, nameList, DateTimeUtils.getTodayDt());
    }

    /**
     * 获取季度指标
     * @param codeList 股票编码列表
     * @param nameList 股票编码列表
     * @param underDate 截止哪天最新的指标
     * @return
     */
    public Map<String, Map<String, MqQuarterMetric>> getLatestQuarterMetric(Collection<String> codeList, Collection<String> nameList, String underDate) {
        Map<String, Map<String, MqQuarterMetric>> codeNameMap = new HashMap<>();
        if (CollectionUtils.isEmpty(codeList)) {
            return codeNameMap;
        }

        List<MqQuarterMetric> metricList = quarterMetricDao.getQuarterLatest(codeList, nameList, underDate);
        for (MqQuarterMetric i: metricList) {
            String tsCode = i.getTsCode();
            String name = i.getName();
            if (!codeNameMap.containsKey(tsCode)) {
                codeNameMap.put(tsCode, new HashMap<>());
            }
            Map<String, MqQuarterMetric> nameMap = codeNameMap.get(tsCode);
            nameMap.put(name, i);
        }

        return codeNameMap;
    }

    public List<MqDailyMetric> getDailyOrderByDateAsc(String tsCode, String metricName) {
        return dailyMetricDao.getTrend(tsCode, metricName);
    }

    public List<MqQuarterMetric> getQuarterByPeriodAsc(String tsCode, String metricName) {
        return quarterMetricDao.getTrend(tsCode, metricName);
    }
}
