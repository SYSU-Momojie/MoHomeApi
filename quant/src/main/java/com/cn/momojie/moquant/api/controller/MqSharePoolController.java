package com.cn.momojie.moquant.api.controller;

import com.cn.momojie.basic.vo.PageResult;
import com.cn.momojie.moquant.api.constant.MqStrategy;
import com.cn.momojie.moquant.api.param.strategy.MqSharePoolParam;
import com.cn.momojie.moquant.api.service.MqSharePoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quant/pool")
public class MqSharePoolController {

    @Autowired
    private MqSharePoolService poolService;

    @RequestMapping(path = "getStrategyPool", method = RequestMethod.POST)
    public PageResult getStrategyPool(@RequestBody MqSharePoolParam param) {
        if (MqStrategy.GROW.equals(param.getStrategy())) {
            return poolService.getGrowList(param);
        } else if (MqStrategy.DIVIDEND.equals(param.getStrategy())) {
            return poolService.getValList(param);
        } else if (MqStrategy.DOWN_GAP.equals(param.getStrategy())) {
            return poolService.getDownGapList(param);
        }

        return PageResult.empty();
    }
}
