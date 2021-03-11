package com.cn.momojie.es.repo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.cn.momojie.es.po.MoTalkLabel;

public interface MoTalkLabelRepo extends ElasticsearchRepository<MoTalkLabel, String> {
}
